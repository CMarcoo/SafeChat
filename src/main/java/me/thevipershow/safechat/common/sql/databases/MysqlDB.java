/*
 * SafeChat - A Minecraft plugin to keep your chat safe.
 * Copyright (C) 2020 TheViperShow
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

package me.thevipershow.safechat.common.sql.databases;

import co.aikar.idb.DB;
import me.thevipershow.safechat.common.sql.DBManager;
import me.thevipershow.safechat.common.sql.SQLUtilities;

public final class MysqlDB extends DBManager {
    public static final String SAVE_ALL = "INSERT INTO safechat_data (player_uuid, player_name, flags_domains, flags_ipv4, flags_words) VALUES (?,?,?,?,?)" +
            " ON DUPLICATE KEY UPDATE player_name = ?, flags_domains = ?, flags_ipv4 = ?, flags_words = ?;";

    /**
     * This method should save all of the data from the database.
     */
    @Override
    public void loadAllData() {
        SQLUtilities.loadData(DB.getGlobalDatabase(), dataManager, SQLiteDB.LOAD_ALL);
    }

    /**
     * This method should transfer all the saved data onto the database.
     */
    @Override
    public void sendAllData() {
        SQLUtilities.loadData(DB.getGlobalDatabase(), dataManager, SAVE_ALL);
    }

    /**
     * This method should create a table if id doesn't exist.
     */
    @Override
    public void createTable() {
        SQLUtilities.loadData(DB.getGlobalDatabase(), dataManager, SQLiteDB.CREATE_TABLE);
    }
}
