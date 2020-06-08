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

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import me.thevipershow.safechat.common.sql.data.Flag;
import me.thevipershow.safechat.common.sql.data.PlayerData;

public interface Database {
    /**
     * Create a table for the database.
     *
     * @return A CompletableFuture of Void type, to indicate the operation has been completed.
     */
    CompletableFuture<Void> createTable();

    /**
     * This method should be implemented this way:
     * It will search through a database and if a UUID was found, its
     * flag should be increased. However if no UUID is found a new row
     * should be added into the database using the values from this method and
     * creating a PlayerData using the static method {@link PlayerData#initializeFromFlag(Flag, String)}
     *
     * @param uuid     The UUID of the player.
     * @param username The username of the player.
     * @param flag     The flag which should be incremented.
     * @return A completableFuture of Integer type,
     * to indicate the operation has been completed and how
     * many rows have been affected.
     */
    CompletableFuture<Integer> doUpdateOrInsert(UUID uuid, String username, Flag flag);

    /**
     * This method is used to clean EVERY row that has a given username
     *
     * @param username The username.
     * @return Returns true if any rows with that username were found, false otherwise.
     */
    CompletableFuture<Boolean> cleanUserData(String username);

    /**
     * This method is used to clean the flag of EVERY row that has a given username
     *
     * @param username The username.
     * @param flag     The Flag to clear.
     * @return Returns true if data has been modified, false otherwise.
     */
    CompletableFuture<Boolean> cleanUserData(String username, Flag flag);

    /**
     * This method is used to retrieve EVERY row that matches the given username as
     * a Set of {@link PlayerData}
     *
     * @param username The username.
     * @return A List with usually 1 PlayerData or more if found, an Empty set if no data was found.
     */
    CompletableFuture<Set<PlayerData>> searchData(String username);

    /**
     * This method is used to retrieve the top PlayerData sorted from highest to low
     * of a given flag.
     *
     * @param flag  A flag to search for in the database.
     * @param count the number of rows to get.
     * @return A Set of PlayerData ordered
     */
    CompletableFuture<List<PlayerData>> topData(Flag flag, int count);
}
