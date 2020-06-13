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

package me.thevipershow.safechat.core.sql.databases;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import lombok.experimental.UtilityClass;
import me.thevipershow.safechat.core.sql.data.Flag;
import me.thevipershow.safechat.core.sql.data.PlayerData;

@UtilityClass
public class DatabaseXUtils {
    public final static String COLUMN_PLAYER_NAME = "player_name";

    public CompletableFuture<Optional<PlayerData>> searchData(final String query, final UUID uuid) {
        return DB.getFirstRowAsync(query, uuid.toString()).thenApplyAsync(dbRow -> {
            if (dbRow.isEmpty()) return Optional.empty();
            final int flagsIpv4 = dbRow.getInt(Flag.IPV4.getRowName());
            final int flagsDomains = dbRow.getInt(Flag.DOMAINS.getRowName());
            final int flagsWords = dbRow.getInt(Flag.WORDS.getRowName());
            final String username = dbRow.getString(COLUMN_PLAYER_NAME);
            return Optional.of(new PlayerData(username, flagsIpv4, flagsDomains, flagsWords));
        });
    }

    public CompletableFuture<Integer> doUpdateOrInsert(final UUID uuid, final String username, final Flag flag, final String query) {
        final PlayerData playerData = PlayerData.initializeFromFlag(flag, username);
        return DB.executeUpdateAsync(query,
                uuid.toString(),
                username,
                playerData.getFlags().get(Flag.DOMAINS),
                playerData.getFlags().get(Flag.IPV4),
                playerData.getFlags().get(Flag.WORDS),
                username);
    }

    public CompletableFuture<Boolean> cleanUserData(final String username, final String query) {
        return DB.executeUpdateAsync(query, username).thenApplyAsync(n -> n != 0);
    }

    public CompletableFuture<Set<PlayerData>> searchData(final String username, final String query) {
        return DB.getResultsAsync(query, username).thenApplyAsync(list -> {
            final Set<PlayerData> playerData = new HashSet<>();
            for (final DbRow dbRow : list)
                playerData.add(new PlayerData(username,
                        dbRow.getInt(Flag.IPV4.getRowName()),
                        dbRow.getInt(Flag.DOMAINS.getRowName()),
                        dbRow.getInt(Flag.WORDS.getRowName())));
            return playerData;
        });
    }

    public CompletableFuture<List<PlayerData>> topData(final int count, final String query) {
        return DB.getResultsAsync(query, count).thenApplyAsync(list -> {
            final LinkedList<PlayerData> playerData = new LinkedList<>();
            for (final DbRow dbRow : list)
                playerData.offer(new PlayerData(dbRow.getString(COLUMN_PLAYER_NAME),
                        dbRow.getInt(Flag.IPV4.getRowName()),
                        dbRow.getInt(Flag.DOMAINS.getRowName()),
                        dbRow.getInt(Flag.WORDS.getRowName())));
            return playerData;
        });
    }

    /**
     * This is an inner utility method used to return a statement depending on the flag that will be used
     *
     * @param flag    The flag which will be used in the statement.
     * @param words   The statement with the words flag.
     * @param domains The statement with the domains flag.
     * @param ipv4    The statement with the ipv4 flag.
     * @return The statement for the corresponding flag.
     * @throws RuntimeException if an unknown flag type was passed.
     */
    public static String getString(Flag flag, String words, String domains, String ipv4) {
        switch (flag) {
            case WORDS:
                return words;
            case DOMAINS:
                return domains;
            case IPV4:
                return ipv4;
            default:
                throw new RuntimeException("bruh");
        }
    }
}
