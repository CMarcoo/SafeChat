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
import java.util.HashMap;
import java.util.UUID;
import static me.thevipershow.safechat.sql.SQLPrebuiltStatements.*;
import org.postgresql.Driver;

public final class PostgreSQLDatabaseManager implements DatabaseManager {
    private static PostgreSQLDatabaseManager instance = null;
    private final HikariDataSource source;

    private PostgreSQLDatabaseManager(String address, int port, String database, String username, String password) {
        this.source = HikariDatabaseUtils.createDataSource(HikariDatabaseUtils.createConfig(address, port, database, username, password, Driver.class, HikariDatabaseUtils.DatabaseType.POSTGRESQL));
    }

    private PostgreSQLDatabaseManager(String address, String database, String username, String password) {
        this(address, 5432, database, username, password);
    }

    public static PostgreSQLDatabaseManager getInstance(String address, int port, String database, String username, String password) {
        return instance != null ? instance : (instance = new PostgreSQLDatabaseManager(address, port, database, username, password));
    }

    public static PostgreSQLDatabaseManager getInstance(String address, String database, String username, String password) {
        return instance != null ? instance : (instance = new PostgreSQLDatabaseManager(address, database, username, password));
    }

    @Override
    public void createTable(ExceptionHandler handler) {
        SQLUtils.createTable(source::getConnection, POSTGRESQL_CREATE_TABLE, handler);
    }

    @Override
    public HashMap<UUID, PlayerData> getAllData(ExceptionHandler handler) {
        return SQLUtils.getAllData(source::getConnection, POSTGRESQL_GET_ALL_DATA, handler);
    }

    @Override
    public void transferAllData(ExceptionHandler handler, HashMap<UUID, PlayerData> dataHashMap) {
        SQLUtils.transferAllData(dataHashMap, source::getConnection, POSTGRESQL_SAVE_ALL_DATA, handler);
    }
}
