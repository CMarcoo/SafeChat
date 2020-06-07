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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import me.thevipershow.safechat.common.sql.data.Flag;
import me.thevipershow.safechat.common.sql.data.PlayerData;

public class SQLiteDatabase implements Database {
    public final static String SQLITE_UPDATE_OR_INSERT = "INSERT INTO safechat_data" +
            " (player_uuid, player_name, flags_domains, flags_ipv4, flags_words)" +
            " ON CONFLICT (player_uuid) DO UPDATE SET" +
            " player_name = ?," +
            " flags_domains = ?," +
            " flags_ipv4 = ?," +
            " flags_words = ?;";

    @org.intellij.lang.annotations.Language("SQL")
    public final static String SQLITE_GET_TOP_DATA = "SELECT player_name, flags_domain, flags_ipv4, flags_words" +
            " ORDER BY ? DESC LIMIT ?;";

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
    public void doUpdateOrInsert(UUID uuid, String username, Flag flag) {
        DB.executeUpdateAsync(SQLITE_UPDATE_OR_INSERT, )
    }

    /**
     * This method is used to clean EVERY row that has a given username
     *
     * @param username The username.
     * @return Returns true if any rows with that username were found, false otherwise.
     */
    @Override
    public CompletableFuture<Boolean> cleanUserData(String username) {
        return null;
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
        return null;
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
        return null;
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
        return DB.getResultsAsync(SQLITE_GET_TOP_DATA, flag.getRowName(), count)
                .thenApplyAsync(list -> {
                    final LinkedList<PlayerData> playerData = new LinkedList<>();
                    list.forEach(dbRow -> {
                        int flagsIpv4 = dbRow.getInt(Flag.IPV4.getRowName());
                        int flagsDomains = dbRow.getInt(Flag.DOMAINS.getRowName());
                        int flagsWords = dbRow.getInt(Flag.WORDS.getRowName());
                        String username = dbRow.getString("player_name");
                        playerData.offer(new PlayerData(username, flagsIpv4, flagsDomains, flagsWords));
                    });
                    return playerData;
                });
    }
}
