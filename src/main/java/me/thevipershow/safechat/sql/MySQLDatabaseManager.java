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

import com.mysql.jdbc.Driver;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.UUID;
import static me.thevipershow.safechat.sql.SQLPrebuiltStatements.*;

public final class MySQLDatabaseManager implements DatabaseManager {
    private static MySQLDatabaseManager instance = null;
    private final HikariDataSource source;

    private MySQLDatabaseManager(String address, int port, String database, String username, String password) {
        this.source = HikariDatabaseUtils.createDataSource(HikariDatabaseUtils.createConfig(address, port, database, username, password, Driver.class, HikariDatabaseUtils.DatabaseType.MYSQL));
    }

    private MySQLDatabaseManager( String address, String database, String username, String password) {
        this( address, 5432, database, username, password);
    }

    public static MySQLDatabaseManager getInstance(String address, String database, String username, String password) {
        return instance != null ? instance : (instance = new MySQLDatabaseManager(address, database, username, password));
    }

    public static MySQLDatabaseManager getInstance(String address, int port, String database, String username, String password) {
        return instance != null ? instance : (instance = new MySQLDatabaseManager(address, port, database, username, password));
    }

    @Override
    public final void createTable(ExceptionHandler handler) {
        SQLUtils.createTable(source::getConnection, MYSQL_CREATE_TABLE, handler);
    }

    @Override
    public HashMap<UUID, PlayerData> getAllData(ExceptionHandler handler) {
        return SQLUtils.getAllData(source::getConnection, MYSQL_GET_ALL_DATA, handler);
    }

    @Override
    public void transferAllData(ExceptionHandler handler, HashMap<UUID, PlayerData> dataHashMap) {
        SQLUtils.transferAllData(dataHashMap, source::getConnection, MYSQL_SAVE_ALL_DATA, handler);
    }
}
