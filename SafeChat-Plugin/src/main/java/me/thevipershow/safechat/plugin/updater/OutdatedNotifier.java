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
package me.thevipershow.safechat.plugin.updater;

import java.util.HashSet;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OutdatedNotifier implements Listener {

    private final HashSet<UUID> alreadyNotified = new HashSet<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!event.getPlayer().isOp() || !event.getPlayer().hasPermission("safechat.updates")) {
            return;
        }
        if (alreadyNotified.contains(uuid)) {
            return;
        }
        event.getPlayer().sendMessage("§8[§6SafeChat§8]§7: §aThere are new updates for SafeChat, please install them!");
        alreadyNotified.add(uuid);
    }
}
