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
import static me.thevipershow.safechat.sql.SQLPrebuiltStatements.*;

public final class MySQLDatabaseManager implements DatabaseManager {
    private static MySQLDatabaseManager instance = null;
    private HikariDataSource source = null;

    private MySQLDatabaseManager(final String address,final int port,final String database,final String username,final String password) {
        try {
            this.source = HikariDatabaseUtils.createDataSource(HikariDatabaseUtils.createConfig(
                    address,
                    port,
                    database,
                    username,
                    password,
                    (Class<? extends Driver>) Class.forName("com.mysql.jdbc.Driver"),
                    HikariDatabaseUtils.DatabaseType.MYSQL));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private MySQLDatabaseManager(final String address,final String database,final String username,final String password) {
        this(address, 5432, database, username, password);
    }

    public static MySQLDatabaseManager getInstance(final String address,final String database,final String username,final String password) {
        return instance != null ? instance : (instance = new MySQLDatabaseManager(address, database, username, password));
    }

    public static MySQLDatabaseManager getInstance(final String address,final int port,final String database,final String username,final String password) {
        return instance != null ? instance : (instance = new MySQLDatabaseManager(address, port, database, username, password));
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
    public void transferAllData(final ExceptionHandler handler,final HashMap<UUID, PlayerData> dataHashMap) {
        SQLUtils.transferAllData(dataHashMap, source::getConnection, MYSQL_SAVE_ALL_DATA, handler);
    }
}
