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

import lombok.experimental.UtilityClass;
import me.thevipershow.safechat.common.configuration.AbstractValues;
import me.thevipershow.safechat.common.configuration.objects.WordsMatcher;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class CheckLogics {
    final String SPLIT_REGEX = "\\s+";
    final String NONE_MATCHER = "CANCEL_EVENT";

    public boolean domainCheck(@NotNull AsyncPlayerChatEvent event, @NotNull AbstractValues values) {
        if (!event.isCancelled() && values.isDomainEnabled()) {
            String text = event.getMessage();
            if (text.length() <= 4)
                return true;
            text = text.replaceAll(values.getDomainWhitelist(), "");
            final String[] splitText = text.split(SPLIT_REGEX);
            for (String s : splitText)
                if (s.matches(values.getDomainRegex())) {
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
    public boolean addressCheck(@NotNull AsyncPlayerChatEvent event, @NotNull AbstractValues values) {
        if (!event.isCancelled() && values.isIpv4Enabled()) {
            String text = event.getMessage();
            if (text.length() < 7) // Save performance, no IPv4s shorter than 7 chars exist
                return true;
            text = text.replaceAll(values.getIpv4Whitelist(), "");
            final String[] splitText = text.split(SPLIT_REGEX);
            for (String s : splitText)
                if (s.matches(values.getIpv4Regex())) {
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
    public boolean wordsCheck(@NotNull AsyncPlayerChatEvent event, @NotNull AbstractValues values) {
        if (!event.isCancelled() && values.isWordsEnabled()) {
            final String[] splitText = event.getMessage().split(SPLIT_REGEX);
            boolean foundAnyMatches = false;
            for (int i = 0; i < splitText.length; i++) {
                final String s = splitText[i];
                for (WordsMatcher matcher : values.getBlacklistWords()) {
                    if (s.matches(matcher.getPattern())) {
                        if (matcher.getReplace().equals(NONE_MATCHER)) {
                            event.setCancelled(true); // If the matcher has pattern "CANCEL_EVENT" we will be immediatly
                            return false;             // returning false and cancelling the event.
                        }
                        splitText[i] = s.replaceAll(matcher.getPattern(), matcher.getReplace());
                        foundAnyMatches = true;
                    }
                }
            }
            event.setMessage(String.join(" ", splitText)); // TODO: 05/06/2020 This could cause some issues with unknown spaces between words
            return !foundAnyMatches;
        }
        return true;
    }
}
