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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public final class SQLiteUtils {

    @FunctionalInterface
    public static interface ExceptionHandler {

        void handle(Exception exception);
    }

    public static boolean createDatabaseFile(File dataFolder) throws IOException {
        boolean success = false;
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        final File file = new File(dataFolder, "safechat.sqlite");
        if (!file.exists()) {
            success = file.createNewFile();
        }
        return success;
    }

    public static Connection getDatabaseConnection(File pluginDataFolderFile) throws SQLException {
        final String url = "jdbc:sqlite:" + pluginDataFolderFile.getAbsolutePath() + File.separator + "safechat.sqlite";
        final Connection con = DriverManager.getConnection(url);
        if (con != null) {
            return con;
        }
        throw new SQLException("Couldn't estabilish a connection");
    }

    public static void createTable(final File dataFolder, final ExceptionHandler handler) {
        try (final Connection connection = getDatabaseConnection(dataFolder)) {
            final String SQL = "CREATE TABLE IF NOT EXISTS safechat_data\n"
                    + "(\n"
                    + "\tplayer_uuid CHARACTER(36) NOT NULL UNIQUE PRIMARY KEY ,\n"
                    + "\tflags INT NOT NULL);";
            try (final PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.executeUpdate();
            }
        } catch (final SQLException exception) {
            handler.handle(exception);
        }
    }

    public static void addUniquePlayerOrUpdate(final File dataFolder, final UUID uuid, final String name, final int severity, final ExceptionHandler handler) {
        try (final Connection connection = getDatabaseConnection(dataFolder)) {
            final String SQL = "INSERT INTO safechat_data (player_uuid, flags) VALUES (?,?) ON CONFLICT (player_uuid) DO UPDATE SET flags = safechat_data.flags + ?;";
            try (final PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.setString(1, uuid.toString());
                statement.setInt(2, severity);
                statement.setInt(3, severity);
                statement.executeUpdate();
            }
        } catch (final SQLException ex) {
            handler.handle(ex);
        }
    }

    public static CompletableFuture<Integer> getPlayerData(final File dataFolder, final UUID searchUUID, final ExecutorService service) {
        final CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        service.submit(() -> {
            try (final Connection connection = getDatabaseConnection(dataFolder)) {
                final String SQL = "SELECT flags FROM safechat_data WHERE player_uuid = ?";
                try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
                    preparedStatement.setString(1, searchUUID.toString());
                    try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                        final int flag = resultSet.getInt("flags");
                        completableFuture.complete(flag);
                    }
                }
            } catch (final SQLException e) {
                completableFuture.completeExceptionally(e);
            }
        });
        return completableFuture;
    }

    public static CompletableFuture<LinkedHashMap<String, Integer>> getTopData(final File dataFolder, final int search, final ExecutorService service) {
        final CompletableFuture<LinkedHashMap<String, Integer>> completableFuture = new CompletableFuture<>();
        service.submit(() -> {
            final LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
            try (final Connection connection = getDatabaseConnection(dataFolder)) {
                final String SQL = "SELECT player_uuid, flags FROM safechat_data ORDER BY flags LIMIT " + search + ";";
                try (final PreparedStatement statement = connection.prepareStatement(SQL)) {
                    try (final ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("player_uuid")));
                            final String foundName = offlinePlayer.getName();
                            final int foundFlags = resultSet.getInt("flags");
                            data.put(foundName, foundFlags);
                        }
                        completableFuture.complete(data);
                    }
                }
            } catch (final SQLException e) {
                completableFuture.completeExceptionally(e);
            }
        });
        return completableFuture;
    }
}
