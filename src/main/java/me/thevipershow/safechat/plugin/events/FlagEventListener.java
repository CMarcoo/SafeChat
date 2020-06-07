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

package me.thevipershow.safechat.plugin.events;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.thevipershow.safechat.common.sql.data.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class FlagEventListener implements Listener {
    private static FlagEventListener instance = null;
    private final JavaPlugin plugin;
    private final DataManager dataManager;

    public static synchronized FlagEventListener getInstance(JavaPlugin plugin, DataManager dataManager) {
        return instance != null ? instance : (new FlagEventListener(plugin, dataManager));
    }

    @EventHandler(ignoreCancelled = true)
    public void onFlag(FlagEvent event) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            if (dataManager.getPlayerData().parallelStream().noneMatch(data -> data.getUuid().equals(event.getUuid()))) {
                dataManager.addPlayerData(PlayerData.initializeFromFlag(event.getFlag(), event.getUuid(), event.getUsername()));
                return;
            }
            dataManager.increasePlayerData(event.getFlag(), event.getUuid());
        });
    }
}
