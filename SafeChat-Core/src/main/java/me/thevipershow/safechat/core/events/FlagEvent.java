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

package me.thevipershow.safechat.core.events;

import java.util.UUID;
import lombok.Getter;
import me.thevipershow.safechat.core.sql.data.Flag;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public final class FlagEvent extends Event {
    private final static HandlerList handlers = new HandlerList();
    private final Flag flag;
    private final UUID uuid;
    private final String username;

    public FlagEvent(Flag flag, UUID uuid, String username) {
        super(true);
        this.flag = flag;
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
