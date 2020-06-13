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

package me.thevipershow.safechat.plugin;

import co.aikar.commands.PaperCommandManager;
import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.idb.DatabaseOptions;
import co.aikar.idb.PooledDatabaseOptions;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.sql.SQLException;
import java.util.Locale;
import lombok.Getter;
import me.thevipershow.safechat.core.checks.CheckManager;
import me.thevipershow.safechat.core.commands.SafeChatCommand;
import me.thevipershow.safechat.core.configuration.AbstractValues;
import me.thevipershow.safechat.core.configuration.ValuesImplementation;
import me.thevipershow.safechat.core.configuration.objects.ExecutableObject;
import me.thevipershow.safechat.core.configuration.objects.WordsMatcher;
import me.thevipershow.safechat.core.events.FlagEventListener;
import me.thevipershow.safechat.core.sql.databases.DatabaseX;
import me.thevipershow.safechat.core.sql.databases.MySQLDatabaseX;
import me.thevipershow.safechat.core.sql.databases.SQLiteDatabaseX;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class SafeChatPlugin extends JavaPlugin {

    private static SafeChatPlugin instance = null;

    /**
     * This methods registers all of the YAML objects that will be read from the config.yml
     */
    private void registerConfigurationSerializer() {
        ConfigurationSerialization.registerClass(ExecutableObject.class);
        ConfigurationSerialization.registerClass(WordsMatcher.class);
    }

    /**
     * This methods creates a new instance of the {@link AbstractValues} class
     * It also checks for eventual issues in the configuration and prints an error
     * if something has gone wrong
     *
     * @return a new AbstractValues object.
     */
    private AbstractValues getAndUpdate() {
        AbstractValues values = ValuesImplementation.getInstance(getConfig(), this);
        try {
            values.updateAll();
        } catch (IllegalArgumentException e) {
            getLogger().warning("Something has went wrong while updating the config.yml");
            e.printStackTrace();
        }
        return values;
    }

    private DatabaseOptions createDatabaseOptions(AbstractValues values) {
        DatabaseOptions.DatabaseOptionsBuilder databaseOptions = DatabaseOptions.builder();
        switch (values.getDbType().toLowerCase(Locale.ROOT)) {
            case "sqlite":
                return databaseOptions.sqlite(getDataFolder().getAbsolutePath() + File.separatorChar + "safechat_data.sqlite")
                        .useOptimizations(true)
                        .logger(getLogger())
                        .build();
            case "mysql":
            case "mariadb":
                return databaseOptions.mysql(values.getUsername(), values.getPassword(), values.getDatabase(), values.getAddress() + ":" + values.getPort())
                        .useOptimizations(true)
                        .logger(getLogger())
                        .build();
            default:
                onDisable(); // the plugin yeets itself if an unknown database type has been chosen.
                throw new IllegalArgumentException("Unknown or invalid database type, disabling plugin.");
        }
    }

    /**
     * Load a {@link Database} from the string name
     *
     * @param dbType The name of the database.
     * @return The Database
     * @throws IllegalArgumentException If the database name is invalid.
     */
    private DatabaseX loadDatabase(String dbType) {
        switch (dbType.toLowerCase(Locale.ROOT)) {
            case "sqlite":
                return SQLiteDatabaseX.getInstance();
            case "mysql":
            case "mariadb":
                return MySQLDatabaseX.getInstance();
            default:
                onDisable();
                throw new IllegalArgumentException("Unknown or invalid database type, disabling plugin.");
        }
    }

    private void createTableInfo(DatabaseX databaseX) {
        try {
            if (databaseX.createTable())
                getLogger().info("The table has been successfully created.");
        } catch (SQLException e) {
            getLogger().warning("Something has went wrong while creating the table.");
            e.printStackTrace();
        }
    }

    @Getter
    private DatabaseX databaseX;

    @Override
    public final void onEnable() { // startup logic:
        instance = this;
        registerConfigurationSerializer();
        saveDefaultConfig();
        AbstractValues values = getAndUpdate();
        CheckManager checkManager = CheckManager.getInstance(this, values);
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerStaticCompletion("checks", ImmutableList.of("domains", "words", "ipv4"));
        DatabaseOptions databaseOptions = createDatabaseOptions(values);
        Database database = PooledDatabaseOptions.builder().options(databaseOptions).createHikariDatabase();
        DB.setGlobalDatabase(database);
        databaseX = loadDatabase(values.getDbType());
        getServer().getPluginManager().registerEvents(checkManager, this);
        getServer().getPluginManager().registerEvents(FlagEventListener.getInstance(this, databaseX, values), this);
        commandManager.registerCommand(SafeChatCommand.getInstance(values, databaseX));

        createTableInfo(databaseX);
    }

    public static SafeChatPlugin getInstance() {
        return instance;
    }
}
