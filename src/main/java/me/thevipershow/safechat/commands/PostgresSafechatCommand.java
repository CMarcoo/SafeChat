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
package me.thevipershow.safechat.commands;

import com.zaxxer.hikari.HikariDataSource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.thevipershow.safechat.config.Values;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class PostgresSafechatCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final HikariDataSource dataSource;
    private final boolean isOnlineMode;
    private final Values values;
    private final ExecutorService service;

    private static PostgresSafechatCommand instance = null;

    private PostgresSafechatCommand(JavaPlugin plugin, HikariDataSource dataSource, boolean isOnlineMode) {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.isOnlineMode = isOnlineMode;
        this.values = Values.getInstance(plugin);
        this.service = Executors.newCachedThreadPool();
    }

    public static PostgresSafechatCommand getInstance(final JavaPlugin plugin, final HikariDataSource dataSource) {
        if (instance == null) {
            instance = new PostgresSafechatCommand(plugin, dataSource, plugin.getServer().getOnlineMode());
        }
        return instance;
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandUtils.processPostgresCommand(args, sender, dataSource, service);
        return true;
    }

}
