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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.swing.plaf.multi.MultiMenuBarUI;
import javax.xml.transform.OutputKeys;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.enums.CheckName;
import me.thevipershow.safechat.enums.TicksConverter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class DataManager {
    private static DataManager instance = null;
    private final DatabaseManager databaseManager;
    private final HashMap<UUID, PlayerData> playerData;
    private final JavaPlugin plugin;
    private final Logger logger;
    private final Values values;

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

    private void startAutomatedSaving() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            databaseManager.transferAllData(e -> {
                logger.log(Level.WARNING, "Something went wrong when saving data into the database!");
                e.printStackTrace();
            }, this.playerData);
        }, TicksConverter.MINUTES.convert(values.getAutoSave()), TicksConverter.MINUTES.convert(values.getAutoSave()));
    }

    public final void addPlayerData(final UUID uuid, final String username, final PlayerData playerData, final CheckName checkName, final int severity) {
        if (!this.playerData.containsKey(uuid)) {
            this.playerData.put(uuid, playerData);
        } else {
            this.playerData.computeIfPresent(uuid, (k, v) -> v.incrementFlag(checkName, severity, username));
        }
    }

    public HashMap<UUID, PlayerData> getPlayerData() {
        return playerData;
    }

    public final List<PlayerData> getTopCheckData(final CheckName checkName, final int top) {
        if (top < 250) {
            return this.playerData.values()
                    .stream()
                    .sorted(Comparator.comparingInt(o -> o.getFlag(checkName)))
                    .limit(top)
                    .collect(Collectors.toUnmodifiableList());
        }
        return null;
    }
}