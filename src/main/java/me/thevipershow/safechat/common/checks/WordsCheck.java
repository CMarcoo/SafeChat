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
import java.util.stream.Collectors;
import me.thevipershow.safechat.common.config.AbstractValues;
import me.thevipershow.safechat.common.config.WordsMatcher;
import me.thevipershow.safechat.common.enums.CheckName;
import me.thevipershow.safechat.common.events.FlagThrownEvent;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class WordsCheck implements ChatCheck {

    private final AbstractValues values;

    private static WordsCheck instance = null;

    private WordsCheck(final AbstractValues values) {
        this.values = values;
    }

    public static WordsCheck getInstance(final AbstractValues values) {
        return instance != null ? instance : (instance = new WordsCheck(values));
    }

    @Override
    public final void result(String message, final AsyncPlayerChatEvent chatEvent) {
        final Player player = chatEvent.getPlayer();
        short flags = 0;
        short replaced = 0;
        for (final WordsMatcher wordsMatcher : values.getBlacklistWords()) {
            final Matcher matcher = wordsMatcher.getCompiledPattern().matcher(message);
            if (matcher.find()) {
                flags++;
                final String replace = wordsMatcher.getReplace();
                if (!replace.equals("NONE")) {
                    replaced++;
                    message = matcher.replaceAll(replace);
                }
            }
        }
        if (flags > 0) {
            Bukkit.getPluginManager().callEvent(new FlagThrownEvent(flags, CheckName.WORDS, player.getUniqueId(), player.getName()));

            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(values.getWordsWarning().stream().map(s -> s.replaceAll("%PLAYER%", player.getName())).collect(Collectors.toList())).color(),
                    TextMessage.build(values.getWordsHover().stream().map(s -> s.replaceAll("%PLAYER%", player.getName())).collect(Collectors.toList())).color()
            ));
            if (replaced > 0) {
                chatEvent.setMessage(message);
            }
        }
    }
}
