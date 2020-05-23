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

package me.thevipershow.safechat.common.sql;

import me.thevipershow.safechat.common.enums.CheckName;

public final class PlayerData {
    private int domainFlags;
    private int ipv4Flags;
    private int wordFlags;
    private String username;

    public PlayerData(final int domainFlags, final int ipv4Flags, final int wordFlags, final String username) {
        this.domainFlags = domainFlags;
        this.ipv4Flags = ipv4Flags;
        this.wordFlags = wordFlags;
        this.username = username;
    }

    public final PlayerData incrementFlag(final CheckName checkName, final int severity, final String username) {
        switch (checkName) {
            case DOMAINS:
                this.domainFlags += severity;
                break;
            case ADDRESSES:
                this.ipv4Flags += severity;
                break;
            case WORDS:
                this.wordFlags += severity;
                break;
        }
        return new PlayerData(this.domainFlags, this.ipv4Flags, this.wordFlags, username);
    }

    public final PlayerData decrementFlag(final CheckName checkName, final int severity, final String username) {
        switch (checkName) {
            case DOMAINS:
                this.domainFlags -= severity;
                break;
            case ADDRESSES:
                this.ipv4Flags -= severity;
                break;
            case WORDS:
                this.wordFlags -= severity;
                break;
        }
        return new PlayerData(this.domainFlags, this.ipv4Flags, this.wordFlags, username);
    }

    public final void resetFlag(final CheckName checkName) {
        switch (checkName) {
            case WORDS:
                this.wordFlags = 0;
                break;
            case DOMAINS:
                this.domainFlags = 0;
                break;
            case ADDRESSES:
                this.ipv4Flags = 0;
                break;
        }
    }

    public final int getFlag(final CheckName checkName) {
        switch (checkName) {
            case WORDS:
                return this.wordFlags;
            case ADDRESSES:
                return this.ipv4Flags;
            case DOMAINS:
                return this.domainFlags;
            default:
                throw new IllegalArgumentException("Unknown Check type of: " + checkName.name());
        }
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

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
