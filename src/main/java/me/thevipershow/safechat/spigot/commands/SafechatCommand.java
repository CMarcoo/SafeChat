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
package me.thevipershow.safechat.spigot.commands;

import me.thevipershow.safechat.common.config.AbstractValues;
import me.thevipershow.safechat.common.sql.DataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class SafechatCommand implements CommandExecutor {
    private final DataManager dataManager;

    private static SafechatCommand instance = null;
    private final AbstractValues values;

    private SafechatCommand(final DataManager dataManager, final AbstractValues values) {
        this.values = values;
        this.dataManager = dataManager;
    }

    public static SafechatCommand getInstance(final DataManager dataManager, final AbstractValues values) {
        if (instance == null) {
            instance = new SafechatCommand(dataManager, values);
        }
        return instance;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        CommandUtils.processCommand(dataManager, args, sender, values);
        return true;
    }
}
