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

package me.thevipershow.safechat.common.sql;

import co.aikar.idb.Database;
import co.aikar.idb.DbRow;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import me.thevipershow.safechat.common.sql.data.Flag;
import me.thevipershow.safechat.common.sql.data.PlayerData;

@SuppressWarnings("LanguageMismatch")
@UtilityClass
public class SQLUtilities {
    public void loadData(Database database, DataManager dataManager, String SQL) {
        try {
            List<DbRow> dbRows = database.getResults(SQL);
            for (DbRow row : dbRows) {
                UUID uuid = UUID.fromString(row.getString("player_uuid"));
                String name = row.getString("player_name");
                int domain = row.getInt("flags_domains");
                int ipv4 = row.getInt("flags_ipv4");
                int words = row.getInt("flags_words");
                dataManager.addPlayerData(new PlayerData(uuid, name, ipv4, domain, words));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendData(Database database, DataManager dataManager, String SQL) {
        for (PlayerData data : dataManager.getPlayerData()) {
            int domains = data.getFlags().get(Flag.DOMAINS);
            int ipv4 = data.getFlags().get(Flag.IPV4);
            int words = data.getFlags().get(Flag.WORDS);
            try {
                database.executeInsert(SQL,
                        data.getUuid(),
                        data.getUsername(),
                        domains,
                        ipv4,
                        words,
                        data.getUsername(),
                        domains,
                        ipv4,
                        words);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTable(Database database, String SQL) {
        try {
            database.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
