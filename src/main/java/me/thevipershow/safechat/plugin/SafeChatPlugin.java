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
import co.aikar.idb.*;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import me.thevipershow.safechat.common.commands.SafeChatCommand;
import me.thevipershow.safechat.common.checks.CheckManager;
import me.thevipershow.safechat.common.configuration.AbstractValues;
import me.thevipershow.safechat.common.configuration.ValuesImplementation;
import me.thevipershow.safechat.common.configuration.objects.ExecutableObject;
import me.thevipershow.safechat.common.configuration.objects.WordsMatcher;
import me.thevipershow.safechat.common.sql.DBManager;
import me.thevipershow.safechat.plugin.events.FlagEventListener;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class SafeChatPlugin extends JavaPlugin {

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
                return databaseOptions.sqlite(getDataFolder().getAbsolutePath() + File.separatorChar + "safechat_data.sqlite").build();
            case "mysql":
                return databaseOptions.mysql(values.getUsername(), values.getPassword(), values.getDatabase(), values.getAddress() + ":" + values.getPort()).build();
            default:
                onDisable(); // the plugin yeets itself if an unknown database type has been chosen.
                throw new IllegalArgumentException("Unknown or invalid database type, disabling plugin.");
        }
    }

    /**
     * This method creates a new database manager later used to handle all the
     * data of this plugin.
     */
//    private DBManager createDatabaseManager(AbstractValues values) {
//        switch (values.getDbType().toLowerCase(Locale.ROOT)) {
//            case "sqlite":
////                createNewFileDatabase();
//                return new SQLiteDB();
//            case "mysql":
//                return new MysqlDB();
//            default:
//                onDisable();
//                throw new IllegalArgumentException("Unknown or invalid database type, disabling plugin.");
//        }
//    }

    /**
     * This method is only used by the SQLite database
     * It creates a database file if missing.
     */
//    private void createNewFileDatabase() {
//        File db = new File(getDataFolder(), "safechat_data.sqlite");
//        if (!db.exists()) {
//            try {
//                db.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private DBManager dbManager;

    @Override
    public final void onEnable() { // startup logic:
        registerConfigurationSerializer();
        saveDefaultConfig();
        AbstractValues values = getAndUpdate();
//        DataManager dataManager = DataManager.getInstance();
        CheckManager checkManager = CheckManager.getInstance(this, values, dataManager);
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerStaticCompletion("checks", ImmutableList.of("domains", "words", "ipv4"));
        commandManager.registerCommand(SafeChatCommand.getInstance(values, dataManager));
        DatabaseOptions databaseOptions = createDatabaseOptions(values);
        Database database = PooledDatabaseOptions.builder().options(databaseOptions).createHikariDatabase();
        DB.setGlobalDatabase(database);
//        dbManager = createDatabaseManager(values);
//        dbManager.createTable();
//        dbManager.loadAllData();
        getServer().getPluginManager().registerEvents(FlagEventListener.getInstance(this, dataManager), this);
    }

    @Override
    public void onDisable() {
        dbManager.sendAllData();
    }
}
