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

public enum SQLPrebuiltStatements {

    SQLITE_CREATE_TABLE("CREATE TABLE IF NOT EXISTS safechat_data\n"
            + "(\n"
            + "\tplayer_uuid CHARACTER(36) NOT NULL UNIQUE PRIMARY KEY ,\n"
            + "\tplayer_name CHARACTER(16) NOT NULL ,\n"
            + "\tflags_words INT NOT NULL ,\n"
            + "\tflags_domains INT NOT NULL ,\n"
            + "\tflags_ipv4 INT NOT NULL);"),
    SQLITE_GET_ALL_DATA("SELECT player_uuid, player_name, flags_domains, flags_ipv4, flags_words FROM safechat_data;"),
    SQLITE_SAVE_ALL_DATA("INSERT INTO safechat_data (player_uuid, player_name, flags_domains, flags_ipv4, flags_words) VALUES (?,?,?,?,?)" +
            " ON CONFLICT (player_uuid) DO UPDATE SET player_name = ?, flags_domains = ?, flags_ipv4 = ?, flags_words = ?;"),
    POSTGRESQL_CREATE_TABLE("CREATE TABLE IF NOT EXISTS safechat_data\n"
            + "(\n"
            + "\tplayer_uuid CHAR(36) NOT NULL UNIQUE PRIMARY KEY ,\n"
            + "\tplayer_name CHAR(16) NOT NULL ,\n"
            + "\tflags_words INT NOT NULL ,\n"
            + "\tflags_domains INT NOT NULL ,\n"
            + "\tflags_ipv4 INT NOT NULL);"),
    POSTGRESQL_GET_ALL_DATA(SQLITE_GET_ALL_DATA.getSQL()),
    POSTGRESQL_SAVE_ALL_DATA(SQLITE_SAVE_ALL_DATA.getSQL()),
    MYSQL_CREATE_TABLE(SQLITE_CREATE_TABLE.getSQL()),
    MYSQL_GET_ALL_DATA(SQLITE_GET_ALL_DATA.getSQL()),
    MYSQL_SAVE_ALL_DATA("INSERT INTO safechat_data (player_uuid, player_name, flags_domains, flags_ipv4, flags_words) VALUES (?,?,?,?,?)" +
            " ON DUPLICATE KEY UPDATE player_name = ?, flags_domains = ?, flags_ipv4 = ?, flags_words = ?;");

    SQLPrebuiltStatements(final String SQL) {
        this.SQL = SQL;
    }

    final String SQL;

    public String getSQL() {
        return SQL;
    }

    public String formatSQL(final Object o) {
        return String.format(SQL, o);
    }
}
