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

import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.thevipershow.safechat.checks.register.CheckRegister;
import me.thevipershow.safechat.commands.SafeChatCommand;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.events.listeners.PostgreSQLFlagListener;
import me.thevipershow.safechat.events.listeners.SQLiteFlagListener;
import me.thevipershow.safechat.sql.PostgreSQLUtils;
import me.thevipershow.safechat.sql.SQLiteUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Safechat extends JavaPlugin {

    public HikariDataSource dataSource;
    private boolean isOnlineMode;
    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private final Values values = Values.getInstance(this);
    private final CheckRegister checkRegister = CheckRegister.getInstance(this);
    private final Logger logger = getLogger();
    private PostgreSQLFlagListener postgreSQLFlagListener;
    private SQLiteFlagListener SQLiteFlagListener;

    @Override
    public void onEnable() {
        isOnlineMode = getServer().getOnlineMode();
        saveDefaultConfig();
        if (values.isEnabled()) {
            if (values.getDbType().equalsIgnoreCase("POSTGRESQL")) {
                dataSource = PostgreSQLUtils.createDataSource(
                        PostgreSQLUtils.createConfig(
                                values.getAddress(),
                                values.getPort(),
                                values.getDatabase(),
                                values.getUsername(),
                                values.getPassword()));

                postgreSQLFlagListener = PostgreSQLFlagListener.getInstance(dataSource, this);
                PostgreSQLUtils.createTable(dataSource, values.getTable());
                pluginManager.registerEvents(postgreSQLFlagListener, this);

            } else if (values.getDbType().equalsIgnoreCase("SQLITE")) {
                try {
                    if (SQLiteUtils.createDatabaseFile(getDataFolder())) {
                        logger.log(Level.INFO, "Succesfully created a new SQLITE database in\n{0}safechat.sqlite", getDataFolder().getAbsolutePath());
                        
                        SQLiteUtils.createTable(this, getDataFolder(), e -> {
                            logger.log(Level.WARNING, "Something went wrong when trying to create table `{0}`\n", values.getTable());
                            e.printStackTrace();
                        });
                        
                    } else {
                        logger.log(Level.INFO, "The SQLITE database has been loaded correctly!");
                    }
                    
                } catch (IOException ex) {
                    logger.log(Level.WARNING, "Could not load the SQLITE database! Something went wrong: \n");
                    ex.printStackTrace();
                }

            }
        }

        pluginManager.registerEvents(checkRegister, this);
        Objects.requireNonNull(getCommand("safechat")).setExecutor(new SafeChatCommand(this, dataSource, isOnlineMode));
    }
}
