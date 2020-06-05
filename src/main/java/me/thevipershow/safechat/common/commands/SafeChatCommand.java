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

package me.thevipershow.safechat.common.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.thevipershow.safechat.common.configuration.AbstractValues;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import sun.awt.UNIXToolkit;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@CommandAlias("safechat")
@Description("&7Main command of the SafeChat plugin")
public final class SafeChatCommand extends BaseCommand {
    private final AbstractValues values;
    private static SafeChatCommand instance = null;
    private final static String PREFIX = "&8[&6SafeChat&8]&f: ";

    public static synchronized SafeChatCommand getInstance(AbstractValues values) {
        return instance != null ? instance : (instance = new SafeChatCommand(values));
    }

    /**
     * Send a colored message to a {@link CommandSender}
     * The colors are given by the '&' placeholder followed by a color char.
     *
     * @param sender  The one who will receive the message.
     * @param message The text as string.
     */
    private static void sendMessage(CommandSender sender, String message) {
        TextAdapter.sendMessage(sender, LegacyComponentSerializer.INSTANCE.deserialize(message, '&'));
    }

    /**
     * Send an array of messages to a {@link CommandSender}
     * The colors are given by the '&' placeholder followed by a color char.
     *
     * @param sender   The one who will receive the message.
     * @param messages The text as an array of strings.
     */
    private static void sendMessages(CommandSender sender, String... messages) {
        for (final String message : messages) {
            sendMessage(sender, message);
        }
    }

    @Subcommand("help")
    @CommandPermission("safechat.commands.help")
    public void sendHelp(CommandSender sender) {
        sendMessages(sender, Messages.HELP_MSG.getS());
    }

    @Subcommand("reload")
    @CommandPermission("safechat.commands.reload")
    public void onReload(CommandSender sender) {
        sendMessage(sender, PREFIX + "&7The configuration will be reloaded");
        try {
            final long then = System.nanoTime();
            this.values.updateAll();
            final long difference = (System.nanoTime() - then) / 1_000_000;
            sendMessage(sender, PREFIX + String.format("&7Configuration has been reloaded correctly in &6%d&7(ms)", difference));
        } catch (IllegalArgumentException e) {
            sendMessage(sender, PREFIX + "&7Configuration could not be updated correctly, issue below:");
            sendMessage(sender, PREFIX + "&c" + e.getMessage());
        }
    }
}
