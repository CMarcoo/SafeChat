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
package me.thevipershow.safechat.enums;

public enum SPermissions {

    BYPASS("safechat.bypass"),
    HELP("safechat.help"),
    RELOAD("safechat.reload"),
    TOP("safechat.top"),
    SEARCH("safechat.search");

    private final String string;

    SPermissions(final String string) {
        this.string = string;
    }

    public final String getConcatPermission(final String permission) {
        return string.concat(".".concat(permission));
    }

    public final String getPermission() {
        return string;
    }
}
