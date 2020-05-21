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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SQLUtils {

    public static void createTable(final ConnectionProvider provider, final SQLPrebuiltStatements sql, final ExceptionHandler handler) {
        try (final Connection c = provider.findConnection()) {
            try (final PreparedStatement s = c.prepareStatement(sql.getSQL())) {
                s.executeUpdate();
            }
        } catch (SQLException e) {
            handler.handle(e);
        }
    }

    public static HashMap<UUID, PlayerData> getAllData(final ConnectionProvider provider, final SQLPrebuiltStatements sql, final ExceptionHandler handler) {
        final HashMap<UUID, PlayerData> data = new HashMap<>();
        try (final Connection connection = provider.findConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(sql.getSQL())) {
                try (final ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        final UUID playerUuid = UUID.fromString(resultSet.getString(TableColumn.UUID_COLUMN.getName()));
                        final String playerName = resultSet.getString(TableColumn.PLAYER_NAME.getName());
                        final int flagsDomain = resultSet.getInt(TableColumn.FLAGS_DOMAINS.getName());
                        final int flagsIpv4 = resultSet.getInt(TableColumn.FLAGS_IPV4.getName());
                        final int flagsWords = resultSet.getInt(TableColumn.FLAGS_WORDS.getName());
                        final PlayerData playerData = new PlayerData(flagsDomain, flagsIpv4, flagsWords, playerName);
                        data.put(playerUuid, playerData);
                    }
                }
            }
        } catch (SQLException e) {
            handler.handle(e);
        }
        return data;
    }

    public static void transferAllData(
            final HashMap<UUID, PlayerData> data,
            final ConnectionProvider provider,
            final SQLPrebuiltStatements sql,
            final ExceptionHandler handler) {
        try (final Connection connection = provider.findConnection()) {
            for (Map.Entry<UUID, PlayerData> entry : data.entrySet()) {
                try (final PreparedStatement statement = connection.prepareStatement(sql.getSQL())) {
                    statement.setString(1, entry.getKey().toString());
                    statement.setString(2, entry.getValue().getUsername());
                    statement.setInt(3, entry.getValue().getDomainFlags());
                    statement.setInt(4, entry.getValue().getIpv4Flags());
                    statement.setInt(5, entry.getValue().getWordFlags());
                    statement.setString(6, entry.getValue().getUsername());
                    statement.setInt(7, entry.getValue().getDomainFlags());
                    statement.setInt(8, entry.getValue().getIpv4Flags());
                    statement.setInt(9, entry.getValue().getWordFlags());
                    int success = statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            handler.handle(e);
        }

    }
}
