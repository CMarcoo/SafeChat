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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
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
        final String url = "jdbc:sqlite:" + pluginDataFolderFile.getAbsolutePath() + "safechat";
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

    public static void createTable(final JavaPlugin plugin, final File dataFolder, final String tableName, final ExceptionHandler handler) {
        scheduler.runTask(plugin, () -> {
            try (final Connection connection = getDatabaseConnection(dataFolder)) {
                final Statement statement = connection.createStatement();
                statement.setQueryTimeout(5);
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + tableName + "`\n"
                        + "(\n"
                        + "\tplayer_uuid CHARACTER(36) NOT NULL UNIQUE PRIMARY KEY ,\n"
                        + "\tusername CHARACTER(16) NOT NULL,\n"
                        + "\tflags INT NOT NULL);");
            } catch (SQLException exception) {
                handler.handle(exception);
            }
        });
    }

    public static void addUniquePlayer(final JavaPlugin plugin, final File dataFolder, final UUID uuid, final int severity, final String tableName, final ExceptionHandler handler) {
        scheduler.runTaskAsynchronously(plugin, () -> {

        });
    }
}
