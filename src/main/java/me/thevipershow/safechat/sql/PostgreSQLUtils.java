/*
 * The MIT License
 *
 * Copyright 2020 marco.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.thevipershow.safechat.sql;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import me.thevipershow.safechat.Safechat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.postgresql.Driver;

public final class PostgreSQLUtils {

    private static final Gson GSON = new Gson();
    private static final String MOJANG_AUTH_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    /**
     * Create an HikariDataSource for PostgreSQL from an HikariConfig
     *
     * @param config a valid config
     * @return a datasource with the PostgreSQL driver
     */
    public static HikariDataSource createDataSource(final HikariConfig config) {
        return new HikariDataSource(config);
    }

    /**
     * Creates a new HikariConfig with the given parameters.
     *
     * @param ip the server address
     * @param database the database name
     * @param username the account's username
     * @param password the account's password
     * @return returns an HikariConfig
     */
    public static HikariConfig createConfig(final String ip, final String database, final String username, final String password) {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql://%s:5432/%s", ip, database));
        return getHikariConfig(username, password, config);
    }

    /**
     * Add values to the Hikari Config passed as argument
     *
     * @param username a valid registered username
     * @param password the account password
     * @param config the HikariConfig that will be modified
     * @return a modified version of the HikariConfig
     */
    private static HikariConfig getHikariConfig(final String username, final String password, final HikariConfig config) {
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(Driver.class.getCanonicalName());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "256");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("allowMultiQueries", "true");
        config.setConnectionTimeout(TimeUnit.SECONDS.toMillis(15L));
        return config;
    }

    public static HikariConfig createConfig(final String ip, final int port, final String database, final String username, final String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql://%s:%d/%s", ip, port, database));
        return getHikariConfig(username, password, config);
    }

    /**
     * Create a table if it doesn't already exist Async [✓]
     *
     * @param source the source
     * @param tableName the name of the table that will be created
     */
    public static void createTable(final HikariDataSource source, final String tableName) {
        Bukkit.getScheduler().runTaskAsynchronously(Safechat.getPlugin(Safechat.class), () -> {
            try {
                try (final Connection connection = source.getConnection(); final PreparedStatement preparedStatement = connection.prepareStatement(
                        "create table if not exists `" + tableName + "`\n"
                        + "(\n"
                        + "\tplayer_uuid uuid not null unique primary key ,\n"
                        + "\tflags int not null);")) {
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        });
    }

    public static void addUniquePlayer(final HikariDataSource source, final UUID uuid, final int severity, final String tableName) {
        Bukkit.getScheduler().runTaskAsynchronously(Safechat.getPlugin(Safechat.class), () -> {
            try {
                try (final Connection connection = source.getConnection(); final PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into `" + tableName + "` (player_uuid, flags) values ('" + uuid.toString() + "',1) on conflict (player_uuid) do update set flags = safechat_data.flags +" + severity + ";"
                )) {
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<LinkedHashMap<UUID, Integer>> getTopData(final HikariDataSource source, final int limit, final String tableName) {
        final CompletableFuture<LinkedHashMap<UUID, Integer>> completableFuture = new CompletableFuture<>();
        EXECUTOR_SERVICE.submit(() -> {
            try {
                try (final Connection connection = source.getConnection(); final PreparedStatement ps = connection.prepareStatement(
                        "select player_uuid, flags from `" + tableName + "` order by flags desc limit " + limit + ";"
                )) {
                    final LinkedHashMap<UUID, Integer> playersFlagsMap = new LinkedHashMap<>();
                    final ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        final UUID uuidOfPlayer = UUID.fromString(resultSet.getString("player_uuid"));
                        final int flagsOfPlayer = resultSet.getInt("flags");
                        playersFlagsMap.put(uuidOfPlayer, flagsOfPlayer);
                    }
                    connection.close();
                    completableFuture.complete(playersFlagsMap);
                }
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        });

        return completableFuture;
    }

    public static CompletableFuture<String> readFromURL(String urlString) {
        final CompletableFuture<String> completableFuture = new CompletableFuture<>();
        EXECUTOR_SERVICE.submit(() -> {
            BufferedReader reader = null;
            try {
                final var url = new URL(urlString);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                final StringBuffer buffer = new StringBuffer();
                int read;
                char[] charsRead = new char[1024];
                while ((read = reader.read(charsRead)) != -1) {
                    buffer.append(charsRead, 0, read);
                }
                completableFuture.complete(buffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
                completableFuture.complete(null);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        return completableFuture;
    }

    private static class AuthResponse {

        private final String id;
        private final String name;

        public AuthResponse(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static CompletableFuture<AuthResponse> getMojangAuthResponse(String playerName) {
        final CompletableFuture<AuthResponse> completableFuture = new CompletableFuture<>();
        EXECUTOR_SERVICE.submit(() -> {
            try {
                final var readJson = readFromURL(MOJANG_AUTH_URL + playerName).get(3250L, TimeUnit.MILLISECONDS);
                if (readJson == null) {
                    completableFuture.complete(null);
                    return;
                }
                final AuthResponse obtainedResponse = GSON.fromJson(readJson, AuthResponse.class);
                completableFuture.complete(obtainedResponse);
            } catch (JsonSyntaxException | InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
                completableFuture.complete(null);
            }
        });
        return completableFuture;
    }

    public static CompletableFuture<Integer> getPlayerScore(final HikariDataSource source, final String name, final boolean onlineMode, final String tableName) {
        final CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        UUID playerUUID;
        if (onlineMode) {
            try {
                final AuthResponse authResponse = getMojangAuthResponse(name).get();
                if (authResponse == null) {
                    completableFuture.complete(null);
                    return completableFuture;
                }
                playerUUID = UUID.fromString(authResponse.id);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                completableFuture.complete(null);
                return completableFuture;
            }
        } else {
            playerUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
        }
        EXECUTOR_SERVICE.submit(() -> {
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if (offlinePlayer.hasPlayedBefore()) {
                try {
                    try (final Connection con = source.getConnection(); final PreparedStatement ps = con.prepareStatement(
                            "select flags from `" + tableName + "` where player_uuid = " + playerUUID.toString() + ";"
                    )) {
                        final ResultSet resultSet = ps.executeQuery();
                        int playerFlags = (resultSet.getInt("flags"));
                        completableFuture.complete(playerFlags);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    completableFuture.complete(null);
                }
            } else {
                completableFuture.complete(null);
            }
        });
        return completableFuture;
    }
}
