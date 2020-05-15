/*
 * The MIT License
 *
 * Copyright 2020 marco.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.thevipershow.safechat;

import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.thevipershow.safechat.checks.register.CheckRegister;
import me.thevipershow.safechat.commands.SafechatCommand;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.config.ValuesValidator;
import me.thevipershow.safechat.config.WordsMatcher;
import me.thevipershow.safechat.enums.ANSIColor;
import me.thevipershow.safechat.events.listeners.FlagListener;
import me.thevipershow.safechat.sql.DatabaseManager;
import me.thevipershow.safechat.sql.PostgreSQLDatabaseManager;
import me.thevipershow.safechat.sql.SQLiteDatabaseManager;
import org.bukkit.Bukkit;
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

    private final String pluginLogo = "&y ____   __   ____  ____  ___  _  _   __  ____ &R\n" +
            "&y/ ___) / _\\ (  __)(  __)/ __)/ )( \\ / _\\(_  _)&R\n" +
            "&y\\___ \\/    \\ ) _)  ) _)( (__ ) __ (/    \\ )(  &R\n" +
            "&y(____/\\_/\\_/(__)  (____)\\___)\\_)(_/\\_/\\_/(__) &R\n" +
            "\n&R&gVersion&R: &b" + getDescription().getVersion() + "&R\n" + "&gAuthor&R: &bTheViperShow&R";

    private void sendInfo() {
        for (final String s : pluginLogo.split("\\n"))
            logger.log(Level.INFO, ANSIColor.colorString('&', s));
        for (final WordsMatcher word : values.getBlacklistWords()) {
            logger.log(Level.INFO, ANSIColor.colorString('&', "Loaded words matcher &yREGEX: &R`&g" + word.getPattern() + "&R` &yPATTERN: &R`&g" + word.getReplace() + "&R`"));
        }
    }

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(WordsMatcher.class, "WordsMatcher");
        saveDefaultConfig();
        values = Values.getInstance(this);
        valuesValidator = ValuesValidator.getInstance(values);
        valuesValidator.validateAll();
        // from here on values are assumed as safe
        switch (values.getDbType().toUpperCase(Locale.getDefault())) {
            case "POSTGRESQL":
                databaseManager = PostgreSQLDatabaseManager.getInstance(this, values.getAddress(), values.getDatabase(), values.getUsername(), values.getPassword());
                break;
            case "SQLITE":
                databaseManager = SQLiteDatabaseManager.getInstance(this);
                break;
        }
        sendInfo();
    }

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("safechat")).setExecutor(safechatCommand = SafechatCommand.getInstance(databaseManager, values));
        pluginManager.registerEvents(FlagListener.getInstance(Objects.requireNonNull(databaseManager), logger, values), this);
        pluginManager.registerEvents(checkRegister = CheckRegister.getInstance(values), this);
    }
}
