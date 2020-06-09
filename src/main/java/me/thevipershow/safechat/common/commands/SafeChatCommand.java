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
import co.aikar.commands.annotation.*;
import co.aikar.commands.annotation.Optional;
import java.util.*;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.thevipershow.safechat.common.configuration.AbstractValues;
import me.thevipershow.safechat.common.sql.data.Flag;
import me.thevipershow.safechat.common.sql.data.PlayerData;
import me.thevipershow.safechat.common.sql.databases.DatabaseX;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@CommandAlias("safechat")
@Description("&7Main command of the SafeChat plugin")
public final class SafeChatCommand extends BaseCommand {
    private final AbstractValues values;
    private final DatabaseX databaseX;

    private static SafeChatCommand instance = null;
    private final static String PREFIX = "&8[&6SafeChat&8]&f: ";

    public static synchronized SafeChatCommand getInstance(AbstractValues values, DatabaseX databaseX) {
        return instance != null ? instance : (instance = new SafeChatCommand(values, databaseX));
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
            sendMessage(sender, PREFIX + String.format("&7Configuration has been reloaded in &6%d&7(ms)", difference));
        } catch (IllegalArgumentException e) {
            sendMessage(sender, PREFIX + "&7Configuration could not be updated correctly, issue below:");
            sendMessage(sender, PREFIX + "&c" + e.getMessage());
        }
    }

    private static void sendData(Set<PlayerData> set, CommandSender sender, String name, Flag f) {
        if (!set.isEmpty()) {
            set.forEach(data -> sendMessage(sender, PREFIX + "&7Player &6" + name + "&7has &6" + data.getFlags().get(f) + " &7flags"));
        } else {
            sendMessage(sender, PREFIX + "&7No data was found for player &6" + name);
        }
    }

    private static void sendFlagMessage(PlayerData data, Flag flag, CommandSender sender) {
        int flags = data.getFlags().get(flag);
        sendMessage(sender, "&7→  &6" + flags + " &7" + flag.name().toLowerCase(Locale.ROOT) + " flags");
    }

    private void dataSearchConsumer(CommandSender sender, String username, Consumer<? super PlayerData> consumer) {
        databaseX.searchData(username).thenAccept(
                set -> {
                    if (set.isEmpty()) {
                        sendMessage(sender, PREFIX + "&7No data was found for this player.");
                        return;
                    }
                    set.forEach(consumer);
                }
        );
    }

    private static String buildPaddedRow(PlayerData data, Flag flag) {
        final StringBuilder builder = new StringBuilder();
        final String username = data.getUsername();
        builder.append("&7").append(username);
        for (int i = 0; i <= 17 - username.length(); i++)
            builder.append(' ');
        builder.append("&6").append(data.getFlags().get(flag));
        return builder.toString();
    }

    private static void sendTopList(CommandSender sender, List<PlayerData> data, Flag flag) {
        sendMessage(sender, "&8[&6NAME&8]               &8[&6FLAGS&8]");
        data.forEach(d ->
                sendMessage(sender, buildPaddedRow(d, flag)));
    }

    @Subcommand("search")
    @Syntax("&8<&6player&8> &8[&6ipv4&7|&6domains&7|&6words&8] &7- Search data of a player.")
    @CommandPermission("safechat.commands.reload")
    @CommandCompletion("@nothing @checks")
    public void onSearch(CommandSender sender, String name, @Optional String flag) {
        if (flag == null) {
            dataSearchConsumer(sender, name, data -> {
                sendMessage(sender, PREFIX + "&7Player &6" + data.getUsername() + "&7 has:");
                Arrays.stream(Flag.values()).forEach(f -> sendFlagMessage(data, f, sender));
            });
        } else {
            try {
                Flag f = Flag.valueOf(flag.toUpperCase(Locale.ROOT));
                dataSearchConsumer(sender, name, data -> sendMessage(sender, PREFIX + "&7Player &6" + data.getUsername() + " &7has &6" + data.getFlags().get(f) + " &7flags."));
            } catch (IllegalArgumentException e) {
                sendMessage(sender, PREFIX + "&7Invalid flag type.");
            }
        }
    }

    @Subcommand("top")
    @Syntax("&8<&6number&8> &8[&6ipv4&7|&6domains&7|&6words&8] &7- Search the top data.")
    @CommandPermission("safechat.commands.top")
    @CommandCompletion("@range:1-100 @checks")
    public void onTop(CommandSender sender, int number, String flag) {
        try {
            if (number < 1 || number > 100) {
                sendMessage(sender, PREFIX + "&7The number is invalid.");
                return;
            }
            Flag f = Flag.valueOf(flag.toUpperCase(Locale.ROOT));
            databaseX.topData(f, number).thenAccept(list -> sendTopList(sender, list, f));
        } catch (IllegalArgumentException e) {
            sendMessage(sender, PREFIX + "&7Invalid flag type.");
        }
    }
}
