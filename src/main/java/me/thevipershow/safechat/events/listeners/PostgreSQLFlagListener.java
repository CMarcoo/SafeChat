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
package me.thevipershow.safechat.events.listeners;

import com.zaxxer.hikari.HikariDataSource;
import java.util.logging.Level;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.safechat.sql.PostgreSQLUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PostgreSQLFlagListener implements Listener {

    private static PostgreSQLFlagListener instance = null;
    private final HikariDataSource dataSource;
    private final JavaPlugin plugin;
    private final Values values;

    private PostgreSQLFlagListener(HikariDataSource dataSource, JavaPlugin plugin) {
        this.dataSource = dataSource;
        this.values = Values.getInstance(plugin);
        this.plugin = plugin;
    }

    public static PostgreSQLFlagListener getInstance(HikariDataSource dataSource, JavaPlugin plugin) {
        if (instance == null) {
            instance = new PostgreSQLFlagListener(dataSource, plugin);
        }
        return instance;
    }

    /**
     * Listener for the PostgreSQL database values
     *
     * @param event whenever a player's message is flagged.
     */
    @EventHandler(ignoreCancelled = true)
    public final void event(FlagThrownEvent event) {
        PostgreSQLUtils.addPlayerOrUpdate(dataSource, e -> {
            plugin.getLogger().log(Level.WARNING, "Something went wrong while trying to update values for {0}!\n", event.getPlayerName());
        }, event.getSenderUUID(), 1);
    }
}
