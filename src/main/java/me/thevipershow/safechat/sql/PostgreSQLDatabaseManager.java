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

package me.thevipershow.safechat.sql;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import static me.thevipershow.safechat.sql.SQLPrebuiltStatements.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.postgresql.Driver;

public final class PostgreSQLDatabaseManager implements DatabaseManager {
    private static PostgreSQLDatabaseManager instance = null;
    private final HikariDataSource source;
    private final JavaPlugin plugin;
    private final BukkitScheduler scheduler;

    private PostgreSQLDatabaseManager(JavaPlugin plugin, String address, int port, String database, String username, String password) {
        this.source = HikariDatabaseUtils.createDataSource(HikariDatabaseUtils.createConfig(address, port, database, username, password, Driver.class, HikariDatabaseUtils.DatabaseType.POSTGRESQL));
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    private PostgreSQLDatabaseManager(JavaPlugin plugin, String address, String database, String username, String password) {
        this(plugin, address, 5432, database, username, password);
    }

    public static PostgreSQLDatabaseManager getInstance(JavaPlugin plugin, String address, int port, String database, String username, String password) {
        return instance != null ? instance : (instance = new PostgreSQLDatabaseManager(plugin, address, port, database, username, password));
    }

    public static PostgreSQLDatabaseManager getInstance(JavaPlugin plugin, String address, String database, String username, String password) {
        return instance != null ? instance : (instance = new PostgreSQLDatabaseManager(plugin, address, database, username, password));
    }

    @Override
    public void createTable(ExceptionHandler handler) {
        SQLUtils.createTable(source::getConnection, POSTGRESQL_CREATE_TABLE, handler);
    }

    @Override
    public void addUniquePlayerOrUpdate(UUID playerUuid, int severity, ExceptionHandler handler) {
        SQLUtils.addUniquePlayerOrUpdate(source::getConnection, POSTGRESQL_ADD_PLAYER_OR_UPDATE, playerUuid, severity, handler);
    }

    @Override
    public CompletableFuture<Integer> getPlayerData(UUID playerUuid) {
        return SQLUtils.getPlayerData(source::getConnection, playerUuid, scheduler, plugin, POSTGRESQL_GET_PLAYER_DATA);
    }

    @Override
    public CompletableFuture<Map<String, Integer>> getTopData(int search) {
        return SQLUtils.getTopData(source::getConnection, scheduler, plugin, POSTGRESQL_GET_TOP_DATA, search);
    }
}
