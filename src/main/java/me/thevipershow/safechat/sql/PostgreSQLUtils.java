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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.postgresql.Driver;

public final class PostgreSQLUtils {

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
     * @param handler handle the exception
     */
    public static void createTable(final HikariDataSource source, final ExceptionHandler handler) {
        try (final Connection connection = source.getConnection()) {
            final String SQL = "CREATE TABLE IF NOT EXISTS safechat_data\n"
                    + "(\n"
                    + "\tplayer_uuid UUID NOT NULL UNIQUE PRIMARY KEY,\n"
                    + "\tflags INT NOT NULL);";
            try (final PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.executeUpdate();
            }
        } catch (final SQLException exception) {
            handler.handle(exception);
        }
    }

    public static void addPlayerOrUpdate(final HikariDataSource source, final ExceptionHandler handler, final UUID playerUUID, final int severity) {
        try (final Connection connection = source.getConnection()) {
            final String SQL = "INSERT INTO safechat_data (player_uuid, flags) VALUES (?,?) ON CONFLICT (player_uuid) DO UPDATE SET flags = safechat_data.flags + ?;";
            try (final PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.setString(1, playerUUID.toString());
                statement.setInt(2, severity);
                statement.setInt(3, severity);
                statement.executeUpdate();
            }
        } catch (final SQLException exception) {
            handler.handle(exception);
        }
    }

    public static CompletableFuture<Integer> getPlayerData(final HikariDataSource source, final UUID searchUUID, final ExecutorService service) {
        final CompletableFuture<Integer> data = new CompletableFuture<>();
        service.submit(() -> {
            try (final Connection connection = source.getConnection()) {
                final String SQL = "SELECT FLAGS FROM safechat_data WHERE player_uuid = ?;";
                try (final PreparedStatement statement = connection.prepareStatement(SQL)) {
                    statement.setString(1, searchUUID.toString());
                    try (final ResultSet resultSet = statement.executeQuery()) {
                        final int flag = resultSet.getInt("flags");
                        data.complete(flag);
                    }
                }
            } catch (SQLException exception) {
                data.completeExceptionally(exception);
            }
        });
        return data;
    }

    public static CompletableFuture<LinkedHashMap<String, Integer>> getTopData(final HikariDataSource source, final int search, final ExecutorService service) {
        final CompletableFuture<LinkedHashMap<String, Integer>> data = new CompletableFuture<>();
        service.submit(() -> {
            try (final Connection connection = source.getConnection()) {
                final String SQL = "SELECT player_uuid, flags FROM safechat_data ORDER BY flags DESC LIMIT " + search + ";";
                try (final PreparedStatement statement = connection.prepareStatement(SQL)) {
                    try (final ResultSet result = statement.executeQuery()) {
                        final LinkedHashMap<String, Integer> results = new LinkedHashMap<>();
                        while (result.next()) {
                            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(result.getString("player_uuid")));
                            final String foundName = offlinePlayer.getName();
                            final int foundFlags = result.getInt("flags");
                            results.put(foundName, foundFlags);
                        }
                        data.complete(results);
                    }
                }
            } catch (SQLException exception) {
                data.completeExceptionally(exception);
            }
        });
        return data;
    }
}
