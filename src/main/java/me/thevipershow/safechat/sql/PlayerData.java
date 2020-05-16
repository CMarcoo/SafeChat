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

package me.thevipershow.safechat.sql;

import java.util.UUID;

public final class PlayerData {
    private final UUID playerUuid;
    private int domainFlags;
    private int ipv4Flags;
    private int wordFlags;

    public PlayerData(final UUID playerUuid, final int domainFlags, final int ipv4Flags, final int wordFlags) {
        this.playerUuid = playerUuid;
        this.domainFlags = domainFlags;
        this.ipv4Flags = ipv4Flags;
        this.wordFlags = wordFlags;
    }

    public final void incrementDomainFlags() {
        domainFlags++;
    }

    public final void incrementIpv4Flags() {
        ipv4Flags++;
    }

    public final void incrementWordFlags() {
        wordFlags++;
    }

    public final void incrementDomainFlags(int value) {
        domainFlags += value;
    }

    public final void incrementIpv4Flags(int value) {
        ipv4Flags += value;
    }

    public final void incrementWordFlags(int value) {
        wordFlags += value;
    }

    public final int getDomainFlags() {
        return domainFlags;
    }

    public final int getIpv4Flags() {
        return ipv4Flags;
    }

    public final int getWordFlags() {
        return wordFlags;
    }
}
