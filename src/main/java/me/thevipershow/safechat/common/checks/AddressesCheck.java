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
package me.thevipershow.safechat.common.checks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.thevipershow.safechat.common.config.AbstractValues;
import me.thevipershow.safechat.common.enums.CheckName;
import me.thevipershow.safechat.common.events.FlagThrownEvent;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class AddressesCheck implements ChatCheck {

    private static AddressesCheck instance = null;
    private final AbstractValues values;

    private AddressesCheck(AbstractValues values) {
        this.values = values;
    }

    public static AddressesCheck getInstance(final AbstractValues values) {
        if (instance == null) {
            instance = new AddressesCheck(values);
        }
        return instance;
    }

    @Override
    public void result(final String message, final AsyncPlayerChatEvent chatEvent) {
        final String stringToCheck = message.replaceAll(values.getIpv4Whitelist(), "");
        final Matcher matcher = Pattern.compile(values.getIpv4Regex()).matcher(stringToCheck);

        if (matcher.find()) {
            chatEvent.setCancelled(true);
            final Player player = chatEvent.getPlayer();
            Bukkit.getPluginManager().callEvent(new FlagThrownEvent(1, CheckName.ADDRESSES, player.getUniqueId(), player.getName()));
            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(values.getIpv4Warning().stream().map(s -> s.replaceAll("%PLAYER%", player.getName())).collect(Collectors.toList())).color(),
                    TextMessage.build(values.getIpv4Hover().stream().map(s -> s.replaceAll("%PLAYER%", player.getName())).collect(Collectors.toList())).color()
                    ));
        }
    }
}
