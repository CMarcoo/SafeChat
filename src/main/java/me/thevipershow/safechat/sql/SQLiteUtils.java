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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public final class SQLiteUtils {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private static final BukkitScheduler scheduler = Bukkit.getScheduler();

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

    @FunctionalInterface
    public static interface ExceptionHandler {

        void handle(Exception exception);
    }

    public static void createTable(final File dataFolder, final ExceptionHandler handler) {
        try (final Connection connection = getDatabaseConnection(dataFolder)) {
            final String SQL = "CREATE TABLE IF NOT EXISTS safechat_data\n"
                    + "(\n"
                    + "\tplayer_uuid CHARACTER(36) NOT NULL UNIQUE PRIMARY KEY ,\n"
                    + "\tusername CHARACTER(16) NOT NULL,\n"
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
            final String SQL = "insert into safechat_data (player_uuid, username, flags) values (?,?,1) on conflict (player_uuid) do update set flags = safechat_data.flags + ?;";
            try (final PreparedStatement statement = connection.prepareStatement(SQL)) {
                statement.setString(1, uuid.toString());
                statement.setString(2, name);
                statement.setInt(3, severity);
                statement.executeUpdate();
            }
        } catch (final SQLException ex) {
            handler.handle(ex);
        }
    }

    public static CompletableFuture<Pair<UUID, Integer>> getPlayerData(final File dataFolder, final String searchName, final ExceptionHandler handler) {
        final CompletableFuture<Pair<UUID, Integer>> completableFuture = new CompletableFuture<>();
        EXECUTOR_SERVICE.submit(() -> {
            try (final Connection connection = getDatabaseConnection(dataFolder)) {
                final String SQL = "SELECT flags FROM safechat_data WHERE username = ?;";
                try (final PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
                    preparedStatement.setString(1, searchName);
                    final ResultSet resultSet = preparedStatement.executeQuery();
                    final int flag = resultSet.getInt("flags");
                    final UUID uuid = UUID.fromString(resultSet.getString("player_uuid"));
                    final Pair<UUID, Integer> pair = new Pair(flag, uuid);
                    completableFuture.complete(pair);
                }
            } catch (final SQLException e) {
                handler.handle(e);
            }
        });
        return completableFuture;
    }

    public static class Pair<X, Y> {

        private final X x;
        private final Y y;

        public Pair(X x, Y y) {
            this.x = x;
            this.y = y;
        }

        public X getX() {
            return x;
        }

        public Y getY() {
            return y;
        }
    }
}
