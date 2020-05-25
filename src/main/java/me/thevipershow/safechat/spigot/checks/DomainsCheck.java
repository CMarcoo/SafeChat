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
package me.thevipershow.safechat.spigot.checks;

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

public final class DomainsCheck implements ChatCheck {

    private final AbstractValues values;

    private static DomainsCheck instance = null;

    private DomainsCheck(final AbstractValues values) {
        this.values = values;
    }

    public static DomainsCheck getInstance(final AbstractValues values) {
        if (instance == null) {
            instance = new DomainsCheck(values);
        }
        return instance;
    }

    @Override
    public void result(final String message, final AsyncPlayerChatEvent chatEvent) {

        final String stringToCheck = message.replaceAll(values.getDomainWhitelist(), "");
        final Matcher matcher = Pattern.compile(values.getDomainRegex()).matcher(stringToCheck);

        if (matcher.find()) {
            final Player player = chatEvent.getPlayer();
            chatEvent.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new FlagThrownEvent(1, CheckName.DOMAINS, player.getUniqueId(), player.getName()));
            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(values.getDomainWarning().stream().map(s -> s.replaceAll("%PLAYER%", player.getName())).collect(Collectors.toList())).color(),
                    TextMessage.build(values.getDomainHover().stream().map(s -> s.replaceAll("%PLAYER%", player.getName())).collect(Collectors.toList())).color()
            ));
        }
    }
}
