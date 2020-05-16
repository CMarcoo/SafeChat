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
            + "\tflags_words INT NOT NULL ,\n"
            + "\tflags_domains INT NOT NULL ,\n"
            + "\tflags_ipv4 INT NOT NULL);"),
    SQLITE_ADD_PLAYER_OR_UPDATE("INSERT INTO safechat_data (player_uuid, flags_%s) VALUES (?,?) ON CONFLICT (player_uuid) DO UPDATE SET flags_%s = safechat_data.flags_%s + ?;"),
    SQLITE_GET_PLAYER_DATA("SELECT flags_%s FROM safechat_data WHERE player_uuid = ?"),
    SQLITE_GET_TOP_DATA("SELECT player_uuid, flags FROM safechat_data ORDER BY flags LIMIT %d;"),
    POSTGRESQL_CREATE_TABLE("CREATE TABLE IF NOT EXISTS safechat_data\n"
            + "(\n"
            + "\tplayer_uuid UUID NOT NULL UNIQUE PRIMARY KEY,\n"
            + "\tflags INT NOT NULL);"),
    POSTGRESQL_ADD_PLAYER_OR_UPDATE("INSERT INTO safechat_data (player_uuid, flags) VALUES (?,?) ON CONFLICT (player_uuid) DO UPDATE SET flags = safechat_data.flags + ?;"),
    POSTGRESQL_GET_PLAYER_DATA("SELECT FLAGS FROM safechat_data WHERE player_uuid = ?;"),
    POSTGRESQL_GET_TOP_DATA("SELECT player_uuid, flags FROM safechat_data ORDER BY flags DESC LIMIT %d;"),
    MYSQL_CREATE_TABLE("CREATE TABLE IF NOT EXISTS safechat_data (\n" +
            "  player_uuid CHAR(36) UNIQUE PRIMARY KEY NOT NULL,\n" +
            "  flags INT NOT NULL\n" +
            ");"),
    MYSQL_ADD_PLAYER_OR_UPDATE("INSERT INTO\n" +
            "  safechat_data (player_uuid, flags)\n" +
            "VALUES\n" +
            "  (?, ?) ON DUPLICATE KEY\n" +
            "UPDATE\n" +
            "  flags = safechat_data.flags + ?;\n");

    SQLPrebuiltStatements(String SQL) {
        this.SQL = SQL;
    }

    final String SQL;

    public String getSQL() {
        return SQL;
    }

    public String formatSQL(Object o) {
        return String.format(SQL, o);
    }
}
