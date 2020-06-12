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

package me.thevipershow.safechat.common.checks;

import java.util.regex.Matcher;
import lombok.experimental.UtilityClass;
import me.thevipershow.safechat.common.configuration.AbstractValues;
import me.thevipershow.safechat.common.configuration.objects.WordsMatcher;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@UtilityClass
public class CheckLogics {
    public final String SPLIT_REGEX = "\\s+";
    public final String NONE_MATCHER = "CANCEL_EVENT";

    /**
     * The domains check:
     * This check is used to find out if a message from an {@link AsyncPlayerChatEvent}
     * contains one or more domains.
     *
     * @param event  This is the chat event.
     * @param values An AbstractValues implementation.
     * @return Returns true if the message was considered 'safe', returns false
     * and cancels the chat event otherwise.
     */
    public boolean domainCheck(AsyncPlayerChatEvent event, AbstractValues values) {
        if (event.getPlayer().hasPermission("safechat.bypasses.domains")) return true;
        if (!values.isDomainEnabled() || event.isCancelled()) return true;
        final String text = event.getMessage();
        if (text.length() <= 4) return true;// no domains shorter than 4 chars should exist.
        final Matcher matcher = values.getDomainRegex().matcher(text);
        while (matcher.find()) {
            final String group = matcher.group();
            if (!group.matches(values.getDomainWhitelist())) {
                event.setCancelled(true);
                return false;
            }
        }
        return true;
    }

    /**
     * The address check:
     * This check is used to find out if a message from an {@link AsyncPlayerChatEvent}
     * contains one or more IPv4 addresses.
     *
     * @param event  This is the chat event
     * @param values An AbstractValues implementation
     * @return returns true if the message was considered 'safe', returns false and cancels the event otherwise.
     */
    public boolean addressCheck(AsyncPlayerChatEvent event, AbstractValues values) {
        if (event.getPlayer().hasPermission("safechat.bypasses.ipv4")) return true;
        if (!values.isIpv4Enabled() || event.isCancelled()) return true;
        final String text = event.getMessage();
        if (text.length() < 7) // Save performance, no IPv4s shorter than 7 chars exist
            return true;
        final Matcher matcher = values.getIpv4Regex().matcher(text);
        while (matcher.find()) {
            final String group = matcher.group();
            if (!group.matches(values.getDomainWhitelist())) {
                event.setCancelled(true);
                return false;
            }
        }
        return true;
    }

    /**
     * The words check:
     * This check is used to find out if a message from an {@link AsyncPlayerChatEvent} contains
     * one or more words that have been marked as 'forbidden'.
     *
     * @param event  This is the chat event.
     * @param values An AbstractValues implementation.
     * @return returns true if the message was considered safe, or has been modified, false if the message was
     * not safe and the event has been cancelled.
     */
    public boolean wordsCheck(AsyncPlayerChatEvent event, AbstractValues values) {
        if (event.getPlayer().hasPermission("safechat.bypasses.words")) return true;
        if (!values.isWordsEnabled() || event.isCancelled()) return true;
        String text = event.getMessage();
        boolean noMatches = true;
        for (final WordsMatcher wm : values.getBlacklistWords()) {
            final Matcher matcher = wm.getPattern().matcher(text);
            if (!matcher.find()) continue;
            if (wm.getReplace().equals("CANCEL_EVENT")) {
                event.setCancelled(true);
                return false;
            }
            text = matcher.replaceAll(wm.getReplace());
            noMatches = false;
        }
        event.setMessage(text);
        return noMatches;
    }
}
