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

public enum TableColumn {
    PLAYER_NAME("player_name"),
    FLAGS_WORDS("flags_words"),
    FLAGS_DOMAINS("flags_domains"),
    FLAGS_IPV4("flags_ipv4"),
    UUID_COLUMN("player_uuid");

    final String name;

    TableColumn(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
