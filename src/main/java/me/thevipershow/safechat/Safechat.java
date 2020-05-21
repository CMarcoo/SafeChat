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
package me.thevipershow.safechat;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.thevipershow.safechat.checks.register.CheckRegister;
import me.thevipershow.safechat.commands.CommandUtils;
import me.thevipershow.safechat.commands.SafechatCommand;
import me.thevipershow.safechat.config.ExecutableObject;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.config.ValuesValidator;
import me.thevipershow.safechat.config.WordsMatcher;
import me.thevipershow.safechat.enums.ANSIColor;
import me.thevipershow.safechat.enums.CheckName;
import me.thevipershow.safechat.events.listeners.FlagListener;
import me.thevipershow.safechat.sql.*;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Safechat extends JavaPlugin {

    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private Values values;
    private ValuesValidator valuesValidator;
    private CheckRegister checkRegister;
    private final Logger logger = getLogger();
    private SafechatCommand safechatCommand;
    private DatabaseManager databaseManager;
    private Commodore commodore;
    private PluginCommand safechatPluginCommand;
    private DataManager dataManager;

    private final String pluginLogo = "&y ____   __   ____  ____  ___  _  _   __  ____ &R\n" +
            "&y/ ___) / _\\ (  __)(  __)/ __)/ )( \\ / _\\(_  _)&R\n" +
            "&y\\___ \\/    \\ ) _)  ) _)( (__ ) __ (/    \\ )(  &R\n" +
            "&y(____/\\_/\\_/(__)  (____)\\___)\\_)(_/\\_/\\_/(__) &R\n" +
            "\n&R&gVersion&R: &b" + getDescription().getVersion() + "&R\n" + "&gAuthor&R: &bTheViperShow&R";

    private void sendInfo() {
        for (final String s : pluginLogo.split("\\n"))
            logger.log(Level.INFO, ANSIColor.colorString('&', s));
        for (final WordsMatcher word : values.getBlacklistWords()) {
            logger.log(Level.INFO, ANSIColor.colorString('&', "Loaded words matcher &yREGEX: &R`&g" + word.getPattern() + "&R` &yREPLACE: &R`&g" + word.getReplace() + "&R`"));
        }
        logExecutables(values.getDomainExecutables(), CheckName.DOMAINS);
        logExecutables(values.getIpv4Executables(), CheckName.ADDRESSES);
        logExecutables(values.getWordsExecutables(), CheckName.WORDS);
    }

    private String joinCommands(final ExecutableObject executableObject) {
        final StringBuilder stringBuilder = new StringBuilder();
        int size = executableObject.getCommands().size();
        for (int i = 0; i < size; i++) {
            if (i != size - 1) {
                stringBuilder.append("\t\t\t     " + executableObject.getCommands().get(i).concat("\n"));
            } else {
                stringBuilder.append("\t\t\t     " + executableObject.getCommands().get(i));
            }
        }
        return stringBuilder.toString();
    }

    private void logExecutables(final List<ExecutableObject> executableObjects, final CheckName checkName) {
        executableObjects.forEach(o -> logger.info(ANSIColor.colorString('&', "Loaded executables for check: `&y" + checkName.name() + "&R` minimum flags: &y" + o.getFlags() + " &Rwith commands:\n" + joinCommands(o))));
    }

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(WordsMatcher.class, "WordsMatcher");
        ConfigurationSerialization.registerClass(ExecutableObject.class, "ExecutableObject");
        saveDefaultConfig();
        saveResource("safechat.commodore", true);
        values = Values.getInstance(this);
        valuesValidator = ValuesValidator.getInstance(values);
        valuesValidator.validateAll();
        // from here on values are assumed as safe
        switch (values.getDbType().toUpperCase(Locale.getDefault())) {
            case "POSTGRESQL":
                databaseManager = PostgreSQLDatabaseManager.getInstance(values.getAddress(), values.getPort(), values.getDatabase(), values.getUsername(), values.getPassword(), this);
                break;
            case "SQLITE":
                databaseManager = SQLiteDatabaseManager.getInstance(this);
                break;
            case "MYSQL":
                databaseManager = MySQLDatabaseManager.getInstance(values.getAddress(), values.getPort(), values.getDatabase(), values.getUsername(), values.getPassword(), this);
        }
        sendInfo();
    }

    @Override
    public void onEnable() {
        dataManager = DataManager.getInstance(databaseManager, this, values);
        if (CommodoreProvider.isSupported()) {
            commodore = CommodoreProvider.getCommodore(this);
        }
        Objects.requireNonNull(safechatPluginCommand = getCommand("safechat")).setExecutor(safechatCommand = SafechatCommand.getInstance(dataManager, values));
        CommandUtils.registerCompletions(commodore, this, e -> {
            logger.log(Level.WARNING, "Something went wrong when enabling command completion");
            e.printStackTrace();
        });
        pluginManager.registerEvents(FlagListener.getInstance(logger, values, dataManager, getServer().getConsoleSender(), this), this);
        pluginManager.registerEvents(checkRegister = CheckRegister.getInstance(values), this);
    }

    @Override
    public void onDisable() {
        logger.log(Level.INFO, "Saving all player data . . .");
        dataManager.transferAllData();
    }
}
