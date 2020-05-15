/*
 * SafeChat - A Minecraft plugin to keep your chat safe.
 *  Copyright (C) 2020 TheViperShow
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
package me.thevipershow.safechat.checks;

import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class AddressesCheck implements ChatCheck {

    private static AddressesCheck instance = null;
    private final Values values;

    private AddressesCheck(Values values) {
        this.values = values;
    }

    public static AddressesCheck getInstance(Values values) {
        if (instance == null) {
            instance = new AddressesCheck(values);
        }
        return instance;
    }

    @Override
    public void result(final String message, final AsyncPlayerChatEvent chatEvent) {
        final String stringToCheck = message.replaceAll(values.getIpv4Whitelist(), "");
        boolean result = stringToCheck.matches(values.getIpv4Regex());

        if (result) {
            chatEvent.setCancelled(true);
            final Player player = chatEvent.getPlayer();
            Bukkit.getPluginManager().callEvent(new FlagThrownEvent(1, "addresses", player.getUniqueId(), player.getName()));
            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(values.getArrayAndReplace(values.getIpv4Warning(), "%PLAYER%", player.getName())).color(),
                    TextMessage.build(values.getArrayAndReplace(values.getIpv4Hover(), "%PLAYER%", player.getName())).color()
            ));
        }
    }
}
