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
package me.thevipershow.safechat.spigot;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.thevipershow.safechat.spigot.checks.CheckRegister;
import me.thevipershow.safechat.common.config.*;
import me.thevipershow.safechat.spigot.commands.CommandUtils;
import me.thevipershow.safechat.spigot.commands.SafechatCommand;
import me.thevipershow.safechat.common.enums.ANSIColor;
import me.thevipershow.safechat.common.enums.CheckName;
import me.thevipershow.safechat.common.events.listeners.FlagListener;
import me.thevipershow.safechat.common.sql.*;
import me.thevipershow.safechat.spigot.config.SpigotValues;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SafeChatSpigot extends JavaPlugin {

    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private SpigotValues values;
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
                stringBuilder.append("\t\t\t     ").append(executableObject.getCommands().get(i).concat("\n"));
            } else {
                stringBuilder.append("\t\t\t     ").append(executableObject.getCommands().get(i));
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
        values = SpigotValues.getInstance(getConfig(), this);
        values.updateAll();
        valuesValidator = ValuesValidator.getInstance(values);
        valuesValidator.validateAll();
        // from here on values are assumed as safe
        switch (values.getDbType().toUpperCase(Locale.getDefault())) {
            case "POSTGRESQL":
                databaseManager = PostgreSQLDatabaseManager.getInstance(values, this);
                break;
            case "SQLITE":
                databaseManager = SQLiteDatabaseManager.getInstance(this);
                break;
            case "MYSQL":
                databaseManager = MySQLDatabaseManager.getInstance(values, this);
            case "MARIADB":
                databaseManager = MariaDBDatabaseManager.getInstance(values, this);
        }
        sendInfo();
    }

    @Override
    public void onEnable() {
        dataManager = DataManager.getInstance(databaseManager, this, values);
        if (CommodoreProvider.isSupported()) {
            commodore = CommodoreProvider.getCommodore(this);
            CommandUtils.registerCompletions(commodore, this, e -> {
                logger.log(Level.WARNING, "Something went wrong when enabling command completion");
                e.printStackTrace();
            });
        }
        Objects.requireNonNull(safechatPluginCommand = getCommand("safechat")).setExecutor(safechatCommand = SafechatCommand.getInstance(dataManager, values));
        pluginManager.registerEvents(FlagListener.getInstance(logger, values, dataManager, getServer().getConsoleSender(), this), this);
        pluginManager.registerEvents(checkRegister = CheckRegister.getInstance(values), this);
    }

    @Override
    public void onDisable() {
        logger.log(Level.INFO, "Saving all player data . . .");
        dataManager.transferAllData();
    }
}
