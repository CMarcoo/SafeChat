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

import java.util.regex.Matcher;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.config.WordsMatcher;
import me.thevipershow.safechat.enums.CheckName;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class WordsCheck implements ChatCheck {

    private final Values values;

    private static WordsCheck instance = null;

    private WordsCheck(Values values) {
        this.values = values;
    }

    public static WordsCheck getInstance(Values values) {
        return instance != null ? instance : (instance = new WordsCheck(values));
    }

    @Override
    public final void result(String message, final AsyncPlayerChatEvent chatEvent) {
        final Player player = chatEvent.getPlayer();
        short flags = 0;
        short replaced = 0;
        for (final WordsMatcher wordsMatcher : values.getBlacklistWords()) {
            final Matcher matcher = wordsMatcher.getCompiledPattern().matcher(message);
            if (matcher.lookingAt()) {
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
                    TextMessage.build(values.getArrayAndReplace(values.getDomainWarning(), "%PLAYER%", player.getName())).color(),
                    TextMessage.build(values.getArrayAndReplace(values.getDomainHover(), "%PLAYER%", player.getName())).color()
            ));
            if (replaced > 0) {
                chatEvent.setMessage(message);
            }
        }
    }
}
