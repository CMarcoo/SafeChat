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

package me.thevipershow.safechat.common.sql.data;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

@Data
public final class PlayerData {
    private final UUID uuid;
    private String username;
    private final Map<Flag, Integer> flags;

    public PlayerData(UUID uuid, String username, int ipv4Flags, int domainsFlags, int wordsFlags) {
        this.uuid = uuid;
        Map<Flag, Integer> map = new EnumMap<>(Flag.class);
        map.put(Flag.WORDS, wordsFlags);
        map.put(Flag.DOMAINS, domainsFlags);
        map.put(Flag.IPV4, ipv4Flags);
        this.flags = map;
    }

    public void updateFlag(Flag flag, int newValue) {
        flags.computeIfPresent(flag, (k, v) -> v = newValue);
    }

    public void increaseFlag(Flag flag) {
        flags.computeIfPresent(flag, (k, v) -> v + 1);
    }

    public void resetFlag(Flag flag) {
        flags.computeIfPresent(flag, (k, v) -> v = 0);
    }

    public void resetAllFlags() {
        flags.forEach((k, v) -> v = 0);
    }

    public static PlayerData initializeFromFlag(Flag flag, UUID uuid, String username) {
        switch (flag) {
            case IPV4:
                return new PlayerData(uuid, username, 1, 0, 0);
            case DOMAINS:
                return new PlayerData(uuid, username, 0, 1, 0);
            case WORDS:
                return new PlayerData(uuid, username, 0, 0, 1);
            default:
                throw new RuntimeException("bruh");
        }
    }
}
