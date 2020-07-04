/*
 * SafeChat - A Minecraft plugin to keep your chat safe.
 * Copyright (C) 2020 TheViperShow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.thevipershow.safechat.core.events;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.thevipershow.safechat.core.configuration.AbstractValues;
import me.thevipershow.safechat.core.sql.data.Flag;
import me.thevipershow.safechat.core.sql.data.PlayerData;
import me.thevipershow.safechat.core.sql.databases.DatabaseX;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class FlagEventListener implements Listener {
    private static FlagEventListener instance = null;
    private final JavaPlugin plugin;
    private final AbstractValues values;
    private final DatabaseX databaseX;

    public static synchronized FlagEventListener getInstance(JavaPlugin plugin, DatabaseX databaseX, AbstractValues values) {
        return instance != null ? instance : (new FlagEventListener(plugin, values, databaseX));
    }

    private void checkFlag(Flag flag, PlayerData data) {
        values.getExecutableOf(flag)
                .forEach(exec -> {
                    if (exec.getFlags() == data.getFlags().get(flag)) {
                        exec.getCommands().stream()
                                .map(str -> str.replace("%PLAYER%", data.getUsername()))
                                .forEach(str -> plugin.getServer().getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str)));
                    }
                });
    }

    @EventHandler(ignoreCancelled = true)
    public void onFlag(FlagEvent event) {
        databaseX.doUpdateOrInsert(event.getUuid(), event.getUsername(), event.getFlag()).thenRun(
                () -> databaseX.searchData(event.getUuid()).thenAccept(opt ->
                        opt.ifPresent(data ->
                                checkFlag(event.getFlag(), data))));
    }
}
