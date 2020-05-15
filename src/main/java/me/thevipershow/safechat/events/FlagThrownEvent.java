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
package me.thevipershow.safechat.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public final class FlagThrownEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final int severity;
    private final String checkName;
    private final UUID senderUUID;
    private final String playerName;

    public FlagThrownEvent(final int severity, final String checkName, final UUID senderUUID, final String playerName) {
        super(true);
        this.severity = severity;
        this.checkName = checkName;
        this.senderUUID = senderUUID;
        this.playerName = playerName;
    }

    public final int getSeverity() {
        return severity;
    }

    public final String getCheckName() {
        return checkName;
    }

    public final UUID getSenderUUID() {
        return senderUUID;
    }

    public final String getPlayerName() {
        return playerName;
    }

    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
