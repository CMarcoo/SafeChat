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
import java.sql.Driver;
import java.util.HashMap;
import java.util.UUID;
import me.thevipershow.safechat.config.Values;
import static me.thevipershow.safechat.sql.SQLPrebuiltStatements.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class MySQLDatabaseManager implements DatabaseManager {
    private static MySQLDatabaseManager instance = null;
    private HikariDataSource source = null;
    private final JavaPlugin plugin;

    private MySQLDatabaseManager(final Values values,
                                 final JavaPlugin plugin) {
        this.plugin = plugin;
        try {
            final Class<? extends Driver> mysqlDriver = (Class<? extends Driver>) Class.forName("com.mysql.jdbc.Driver");
            this.source = HikariDatabaseUtils.createDataSource(HikariDatabaseUtils.createConfig(
                    values.getAddress(),
                    values.getPort(),
                    values.getDatabase(),
                    values.getUsername(),
                    values.getPassword(),
                    mysqlDriver,
                    HikariDatabaseUtils.DatabaseType.MYSQL));
            this.createTable(e -> {
                plugin.getLogger().warning("Safechat could not create the table successfully!");
                e.printStackTrace();
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static MySQLDatabaseManager getInstance(final Values values,
                                                   final JavaPlugin plugin) {
        return instance != null ? instance : (instance = new MySQLDatabaseManager(values, plugin));
    }

    @Override
    public final void createTable(final ExceptionHandler handler) {
        SQLUtils.createTable(source::getConnection, MYSQL_CREATE_TABLE, handler);
    }

    @Override
    public HashMap<UUID, PlayerData> getAllData(final ExceptionHandler handler) {
        return SQLUtils.getAllData(source::getConnection, MYSQL_GET_ALL_DATA, handler);
    }

    @Override
    public void transferAllData(final ExceptionHandler handler, final HashMap<UUID, PlayerData> dataHashMap) {
        SQLUtils.transferAllData(dataHashMap, source::getConnection, MYSQL_SAVE_ALL_DATA, handler);
    }
}
