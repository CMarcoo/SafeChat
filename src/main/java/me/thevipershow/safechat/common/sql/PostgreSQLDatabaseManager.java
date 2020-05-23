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

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.UUID;
import me.thevipershow.safechat.common.config.Values;
import static me.thevipershow.safechat.common.sql.SQLPrebuiltStatements.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.postgresql.Driver;

public final class PostgreSQLDatabaseManager implements DatabaseManager {
    private static PostgreSQLDatabaseManager instance = null;
    private final HikariDataSource source;
    private final JavaPlugin plugin;

    private PostgreSQLDatabaseManager(final Values values,
                                      final JavaPlugin plugin) {
        this.plugin = plugin;
        this.source = HikariDatabaseUtils.createDataSource(HikariDatabaseUtils.createConfig(
                values.getAddress(),
                values.getPort(),
                values.getDatabase(),
                values.getUsername(),
                values.getPassword(),
                Driver.class,
                HikariDatabaseUtils.DatabaseType.POSTGRESQL));
        this.createTable(e -> {
            plugin.getLogger().warning("SafeChat could not create the table successfully!");
            e.printStackTrace();
        });
    }

    public static PostgreSQLDatabaseManager getInstance(final Values values,
                                                        final JavaPlugin plugin) {
        return instance != null ? instance : (instance = new PostgreSQLDatabaseManager(values, plugin));
    }

    @Override
    public void createTable(final ExceptionHandler handler) {
        SQLUtils.createTable(source::getConnection, POSTGRESQL_CREATE_TABLE, handler);
    }

    @Override
    public HashMap<UUID, PlayerData> getAllData(final ExceptionHandler handler) {
        return SQLUtils.getAllData(source::getConnection, POSTGRESQL_GET_ALL_DATA, handler);
    }

    @Override
    public void transferAllData(final ExceptionHandler handler, final HashMap<UUID, PlayerData> dataHashMap) {
        SQLUtils.transferAllData(dataHashMap, source::getConnection, POSTGRESQL_SAVE_ALL_DATA, handler);
    }
}
