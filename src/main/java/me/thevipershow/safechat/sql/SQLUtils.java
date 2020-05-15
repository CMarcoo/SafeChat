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
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class SQLUtils {

    private final static String FLAGS_COLUMN = "flags";
    private final static String UUID_COLUMN = "player_uuid";

    public static void createTable(final ConnectionProvider provider, final SQLPrebuiltStatements sql, final ExceptionHandler handler) {
        try (final Connection c = provider.findConnection()) {
            try (final PreparedStatement s = c.prepareStatement(sql.getSQL())) {
                s.executeUpdate();
            }
        } catch (SQLException e) {
            handler.handle(e);
        }
    }

    public static void addUniquePlayerOrUpdate(final ConnectionProvider provider, final SQLPrebuiltStatements sql, final UUID playerUUID, final int severity, final ExceptionHandler handler) {
        try (final Connection c = provider.findConnection()) {
            try (final PreparedStatement s = c.prepareStatement(sql.getSQL())) {
                s.setString(1, playerUUID.toString());
                s.setInt(2, severity);
                s.setInt(3, severity);
                s.executeUpdate();
            }
        } catch (SQLException e) {
            handler.handle(e);
        }
    }

    public static CompletableFuture<Integer> getPlayerData(final ConnectionProvider provider, final UUID playerUuid, final BukkitScheduler scheduler, final JavaPlugin plugin, final SQLPrebuiltStatements sql) {
        final CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        scheduler.runTaskAsynchronously(plugin, () -> {
            try (final Connection c = provider.findConnection()) {
                try (final PreparedStatement s = c.prepareStatement(sql.getSQL())) {
                    s.setString(1, playerUuid.toString());
                    try (final ResultSet rs = s.executeQuery()) {
                        final int flags = rs.getInt(FLAGS_COLUMN);
                        completableFuture.complete(flags);
                    }
                }
            } catch (SQLException e) {
                completableFuture.completeExceptionally(e);
            }
        });
        return completableFuture;
    }

    public static CompletableFuture<Map<String, Integer>> getTopData(final ConnectionProvider provider, final BukkitScheduler scheduler, final JavaPlugin plugin, final SQLPrebuiltStatements sql, final Object search) {
        final CompletableFuture<Map<String, Integer>> completableFuture = new CompletableFuture<>();
        scheduler.runTaskAsynchronously(plugin, () -> {
            try (final Connection c = provider.findConnection()) {
                try (final PreparedStatement s = c.prepareStatement(sql.formatSQL(search))) {
                    try (final ResultSet rs = s.executeQuery()) {
                        final Map<String, Integer> data = new HashMap<>();
                        while (rs.next()) {
                            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString(UUID_COLUMN)));
                            final String foundName = offlinePlayer.getName();
                            final int foundFlags = rs.getInt(FLAGS_COLUMN);
                            data.put(foundName, foundFlags);
                        }
                        completableFuture.complete(data);
                    }
                }
            } catch (SQLException e) {
                completableFuture.completeExceptionally(e);
            }
        });
        return completableFuture;
    }
}
