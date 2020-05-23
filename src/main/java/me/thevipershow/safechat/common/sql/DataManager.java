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

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import me.thevipershow.safechat.common.config.ExecutableObject;
import me.thevipershow.safechat.common.config.Values;
import me.thevipershow.safechat.common.enums.CheckName;
import me.thevipershow.safechat.common.enums.TicksConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class DataManager {
    private static DataManager instance = null;
    private final DatabaseManager databaseManager;
    private final HashMap<UUID, PlayerData> playerData;
    private final JavaPlugin plugin;
    private final Logger logger;
    private final Values values;
    private boolean working = false;

    private DataManager(final DatabaseManager databaseManager, final JavaPlugin plugin, final Values values) {
        this.databaseManager = databaseManager;
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.values = values;
        this.playerData = databaseManager.getAllData(e -> {
            logger.log(Level.WARNING, "Something went wrong when loading data from the database!");
            e.printStackTrace();
        });
        startAutomatedSaving();
    }

    public static DataManager getInstance(final DatabaseManager databaseManager, final JavaPlugin plugin, final Values values) {
        return instance != null ? instance : (instance = new DataManager(databaseManager, plugin, values));
    }

    public void transferAllData() {
        if (working) {
            long waited = 25;
            do {
                try {
                    wait(25);
                    waited += 25;
                    if (waited >= 1500) {
                        logger.warning("Database is not responding correctly, closing connections.");
                        plugin.getServer().getScheduler().cancelTasks(plugin);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (working);
        }
        working = true;
        databaseManager.transferAllData(e -> {
            logger.log(Level.WARNING, "Something went wrong when saving data into the database!");
            e.printStackTrace();
        }, this.playerData);
        working = false;
    }

    private void startAutomatedSaving() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::transferAllData, TicksConverter.MINUTES.convert(values.getAutoSave()), TicksConverter.MINUTES.convert(values.getAutoSave()));
    }

    public int removeAllPlayer(final String username) {
        int removed = 0;
        for (final Map.Entry<UUID, PlayerData> entry : this.playerData.entrySet()) {
            if (entry.getValue().getUsername().equals(username)) {
                this.playerData.remove(entry.getKey());
                removed++;
            }
        }
        return removed;
    }

    public int removeAllPlayer(final String username, final CheckName checkName) {
        int modified = 0;
        for (final Map.Entry<UUID, PlayerData> entry : this.playerData.entrySet()) {
            if (entry.getValue().getUsername().equals(username)) {
                this.playerData.get(entry.getKey()).resetFlag(checkName);
                modified++;
            }
        }
        return modified;
    }

    public final void addPlayerData(final UUID uuid, final String username, final PlayerData playerData, final CheckName checkName, final int severity) {
        if (!this.playerData.containsKey(uuid)) {
            this.playerData.put(uuid, playerData);
        } else {
            this.playerData.computeIfPresent(uuid, (k, v) -> v.incrementFlag(checkName, severity, username));
        }
    }

    public final List<EnumMap<CheckName, Integer>> getPlayerFlags(final String playerName) {
        List<PlayerData> playerDataStream = this.playerData.values().stream().filter(e -> e.getUsername().equals(playerName)).collect(Collectors.toList());
        if (playerDataStream.size() > 0) {
            final List<EnumMap<CheckName, Integer>> enumMaps = new ArrayList<>();
            playerDataStream.forEach(c -> {
                final EnumMap<CheckName, Integer> checkNameIntegerEnumMap = new EnumMap<>(CheckName.class);
                for (final CheckName checkName : CheckName.values()) {
                    checkNameIntegerEnumMap.put(checkName, c.getFlag(checkName));
                }
                enumMaps.add(checkNameIntegerEnumMap);
            });
            return enumMaps;
        }
        return null;
    }

    public final List<PlayerData> getPlayerFlags(final String playerName, final CheckName checkName) {
        List<PlayerData> playerData = this.playerData.values().stream().filter(e -> e.getUsername().equals(playerName)).collect(Collectors.toList());
        if (playerData.size() > 0) {
            return playerData;
        }
        return null;
    }

    public final List<PlayerData> getTopCheckData(final CheckName checkName, final int top) {
        if (top < 250) {
            return this.playerData.values()
                    .stream()
                    .filter(c -> c.getFlag(checkName) != 0)
                    .sorted(Comparator.comparingInt(o -> o.getFlag(checkName)))
                    .limit(top)
                    .collect(Collectors.toList());
        }
        return null;
    }

    private void dispatchCommands(final List<String> commands,
                                  final String username,
                                  final ConsoleCommandSender console) {
        for (final String command : commands) {
            Bukkit.dispatchCommand(console, command.replaceAll("%PLAYER%", username));
        }
    }

    private boolean checkFlags(final List<ExecutableObject> executableObjects,
                               final CheckName checkName,
                               final PlayerData data,
                               final String username,
                               final ConsoleCommandSender console) {
        for (final ExecutableObject e : executableObjects) {
            if (e.getFlags() == data.getFlag(checkName)) {
                dispatchCommands(e.getCommands(), username, console);
                return true;
            }
        }
        return false;
    }

    public final void checkExecute(final UUID uuid,
                                   final String username,
                                   final List<ExecutableObject> wordsExecutable,
                                   final List<ExecutableObject> ipv4Executable,
                                   final List<ExecutableObject> domainsExecutable,
                                   final ConsoleCommandSender console) {
        final PlayerData matchingData = this.playerData.get(uuid);
        if (matchingData != null) {
            if (!checkFlags(wordsExecutable, CheckName.WORDS, matchingData, username, console)) {
                if (!checkFlags(domainsExecutable, CheckName.DOMAINS, matchingData, username, console)) {
                    checkFlags(ipv4Executable, CheckName.ADDRESSES, matchingData, username, console);
                }
            }
        }
    }
}
