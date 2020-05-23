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
package me.thevipershow.safechat.common.sql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import static me.thevipershow.safechat.common.sql.SQLPrebuiltStatements.*;


public final class SQLiteDatabaseManager implements DatabaseManager {

    private static SQLiteDatabaseManager instance = null;
    private final JavaPlugin plugin;
    private final File dataFolder;
    private final BukkitScheduler scheduler;

    private SQLiteDatabaseManager(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        this.scheduler = plugin.getServer().getScheduler();
        createDatabaseFile(plugin.getDataFolder());
        createTable(e -> {
            plugin.getLogger().log(Level.WARNING, "Something went wrong when creating table!");
            e.printStackTrace();
        });
    }

    public final boolean createDatabaseFile(final File dataFolder) {
        boolean success = false;
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        final File file = new File(dataFolder, "safechat.sqlite");
        if (!file.exists()) {
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    private Connection getDatabaseConnection() throws SQLException {
        final String url = "jdbc:sqlite:" + this.dataFolder.getAbsolutePath() + File.separator + "safechat.sqlite";
        final Connection con = DriverManager.getConnection(url);
        if (con != null) {
            return con;
        }
        throw new SQLException("Couldn't establish a connection");
    }

    public static SQLiteDatabaseManager getInstance(final JavaPlugin plugin) {
        return instance != null ? instance : (instance = new SQLiteDatabaseManager(plugin));
    }

    @Override
    public final void createTable(final ExceptionHandler handler) {
        SQLUtils.createTable(this::getDatabaseConnection, SQLITE_CREATE_TABLE, handler);
    }

    @Override
    public HashMap<UUID, PlayerData> getAllData(final ExceptionHandler handler) {
        return SQLUtils.getAllData(this::getDatabaseConnection, SQLITE_GET_ALL_DATA, handler);
    }

    @Override
    public void transferAllData(final ExceptionHandler handler, final HashMap<UUID, PlayerData> dataHashMap) {
        SQLUtils.transferAllData(dataHashMap, this::getDatabaseConnection, SQLITE_SAVE_ALL_DATA, handler);
    }
}
