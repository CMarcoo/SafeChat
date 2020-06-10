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

package me.thevipershow.safechat.common.sql.databases;

import co.aikar.idb.DB;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.thevipershow.safechat.common.sql.data.Flag;
import me.thevipershow.safechat.common.sql.data.PlayerData;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SQLiteDatabaseX implements DatabaseX {
    private static SQLiteDatabaseX instance;

    public static SQLiteDatabaseX getInstance() {
        return instance != null ? instance : (instance = new SQLiteDatabaseX());
    }


    public final static String SQLITE_UPDATE_OR_INSERT_DOMAINS = "INSERT INTO safechat_data(player_uuid, player_name, flags_domains, flags_ipv4, flags_words)\n" +
            "VALUES (?, ?, ?, ?, ?)\n" +
            "ON CONFLICT(player_uuid)\n" +
            "    DO UPDATE SET flags_domains = flags_domains + 1, player_name = ?;";
    public final static String SQLITE_UPDATE_OR_INSERT_IPV4 = "INSERT INTO safechat_data(player_uuid, player_name, flags_domains, flags_ipv4, flags_words)\n" +
            "VALUES (?, ?, ?, ?, ?)\n" +
            "ON CONFLICT(player_uuid)\n" +
            "    DO UPDATE SET flags_ipv4 = flags_ipv4 + 1, player_name = ?;";
    public final static String SQLITE_UPDATE_OR_INSERT_WORDS = "INSERT INTO safechat_data(player_uuid, player_name, flags_domains, flags_ipv4, flags_words)\n" +
            "VALUES (?, ?, ?, ?, ?)\n" +
            "ON CONFLICT(player_uuid)\n" +
            "    DO UPDATE SET flags_words = flags_words + 1, player_name = ?;";

    public final static String SQLITE_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS safechat_data\n" +
            "(\n" +
            "    player_uuid   CHARACTER(36) NOT NULL UNIQUE PRIMARY KEY,\n" +
            "    player_name   CHARACTER(16) NOT NULL,\n" +
            "    flags_words   INT           NOT NULL,\n" +
            "    flags_domains INT           NOT NULL,\n" +
            "    flags_ipv4    INT           NOT NULL\n" +
            ");";
    public final static String SQLITE_GET_TOP_DATA_DOMAINS = "SELECT player_name, flags_domains, flags_ipv4, flags_words" +
            " FROM safechat_data ORDER BY flags_domains DESC LIMIT ?;";

    public final static String SQLITE_GET_TOP_DATA_IPV4 = "SELECT player_name, flags_domains, flags_ipv4, flags_words" +
            " FROM safechat_data ORDER BY flags_ipv4 DESC LIMIT ?;";

    public final static String SQLITE_GET_TOP_DATA_WORDS = "SELECT player_name, flags_domains, flags_ipv4, flags_words" +
            " FROM safechat_data ORDER BY flags_words DESC LIMIT ?;";

    public final static String SQLITE_GET_USERNAME_DATA = "SELECT flags_domains, flags_ipv4, flags_words" +
            " FROM safechat_data WHERE player_name = ?;";

    public final static String SQLITE_DELETE_ALL_DATA = "DELETE FROM safechat_data WHERE player_name = ?";

    public final static String SQLITE_RESET_FLAG_DATA_DOMAINS = "UPDATE safechat_data SET flags_domains = 0 WHERE player_name = ?";

    public final static String SQLITE_RESET_FLAG_DATA_IPV4 = "UPDATE safechat_data SET flags_ipv4 = 0 WHERE player_name = ?";

    public final static String SQLITE_RESET_FLAG_DATA_WORDS = "UPDATE safechat_data SET flags_words = 0 WHERE player_name = ?";

    public final static String SQLITE_GET_UUID_DATA = "SELECT flags_domains, flags_ipv4, flags_words, player_name" +
            " FROM safechat_data WHERE player_uuid = ?;";

    public static String getUpdateOrInsertStatement(Flag flag) {
        return DatabaseXUtils.getString(flag, SQLITE_UPDATE_OR_INSERT_WORDS, SQLITE_UPDATE_OR_INSERT_DOMAINS, SQLITE_UPDATE_OR_INSERT_IPV4);
    }

    public static String getResetFlagDataStatement(Flag flag) {
        return DatabaseXUtils.getString(flag, SQLITE_RESET_FLAG_DATA_WORDS, SQLITE_RESET_FLAG_DATA_DOMAINS, SQLITE_RESET_FLAG_DATA_IPV4);
    }

    public static String getTopDataStatement(Flag flag) {
        return DatabaseXUtils.getString(flag, SQLITE_GET_TOP_DATA_WORDS, SQLITE_GET_TOP_DATA_DOMAINS, SQLITE_GET_TOP_DATA_IPV4);
    }

    /**
     * Create a table for the database.
     *
     * @return A CompletableFuture of Void type, to indicate the operation has been completed.
     * @throws SQLException If something went wrong while trying to create the table.
     */
    @Override
    public boolean createTable() throws SQLException {
        return DB.executeUpdate(SQLITE_CREATE_TABLE) == 1;
    }

    /**
     * Return the exact PlayerData of a UUID.
     *
     * @param uuid The uuid to search for.
     * @return The PlayerData Optional.
     */
    @Override
    public CompletableFuture<Optional<PlayerData>> searchData(UUID uuid) {
        return DatabaseXUtils.searchData(SQLITE_GET_UUID_DATA, uuid);
    }

    /**
     * This method should be implemented this way:
     * It will search through a database and if a UUID was found, its
     * flag should be increased. However if no UUID is found a new row
     * should be added into the database using the values from this method and
     * creating a PlayerData using the static method {@link PlayerData#initializeFromFlag(Flag, String)}
     *
     * @param uuid     The UUID of the player.
     * @param username The username of the player (will only be used in case of missing data).
     * @param flag     The flag which should be incremented.
     */
    @Override
    public CompletableFuture<Integer> doUpdateOrInsert(UUID uuid, String username, Flag flag) {
        return DatabaseXUtils.doUpdateOrInsert(uuid, username, flag, getUpdateOrInsertStatement(flag));
    }

    /**
     * This method is used to clean EVERY row that has a given username
     *
     * @param username The username.
     * @return Returns true if any rows with that username were found, false otherwise.
     */
    @Override
    public CompletableFuture<Boolean> cleanUserData(String username) {
        return DatabaseXUtils.cleanUserData(username, SQLITE_DELETE_ALL_DATA);
    }

    /**
     * This method is used to clean the flag of EVERY row that has a given username
     *
     * @param username The username.
     * @param flag     The Flag to clear.
     * @return Returns true if data has been modified, false otherwise.
     */
    @Override
    public CompletableFuture<Boolean> cleanUserData(String username, Flag flag) {
        return DatabaseXUtils.cleanUserData(username, getResetFlagDataStatement(flag));
    }

    /**
     * This method is used to retrieve EVERY row that matches the given username as
     * a Set of {@link PlayerData}
     *
     * @param username The username.
     * @return A Set with usually 1 PlayerData or more if found, an Empty set if no data was found.
     */
    @Override
    public CompletableFuture<Set<PlayerData>> searchData(String username) {
        return DatabaseXUtils.searchData(username, SQLITE_GET_USERNAME_DATA);
    }

    /**
     * This method is used to retrieve the top PlayerData sorted from highest to low
     * of a given flag.
     *
     * @param flag  A flag to search for in the database.
     * @param count the number of rows to get.
     * @return A Set of PlayerData ordered
     */
    @Override
    public CompletableFuture<List<PlayerData>> topData(Flag flag, int count) {
        return DatabaseXUtils.topData(count, getTopDataStatement(flag));
    }
}
