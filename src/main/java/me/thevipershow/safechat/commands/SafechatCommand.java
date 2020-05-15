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
package me.thevipershow.safechat.commands;

import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.sql.DatabaseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class SafechatCommand implements CommandExecutor {
    private final DatabaseManager databaseManager;

    private static SafechatCommand instance = null;
    private final Values values;

    private SafechatCommand(DatabaseManager databaseManager, Values values) {
        this.values = values;
        this.databaseManager = databaseManager;
    }

    public static SafechatCommand getInstance(final DatabaseManager databaseManager, Values values) {
        if (instance == null) {
            instance = new SafechatCommand(databaseManager, values);
        }
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandUtils.processCommand(databaseManager, args, sender, values);
        return true;
    }
}
