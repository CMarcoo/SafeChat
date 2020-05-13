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

import java.io.File;
import java.util.List;
import me.thevipershow.safechat.enums.HoverMessages;
import me.thevipershow.safechat.enums.SPermissions;
import me.thevipershow.safechat.sql.SQLiteUtils;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author marco
 */
public final class CommandUtils {

    public static void noArguments(final CommandSender commandSender) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            player.spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build("&7» &eNo arguments found &7. . .", ""
                            + "&7Hover &nhere &r&7to view all commands &8[&f*&8]").color(),
                    TextMessage.build(HoverMessages.NO_ARGS.getMessages()).color()
            ));
        } else {
            commandSender.sendMessage(TextMessage.build(HoverMessages.NO_ARGS.getMessages()).color().getText());
        }
    }

    public static void sendWarning(final CommandSender commandSender, final String error) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            player.spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build("&7Â» &4Your command was incorrect!", "&7Hover here to see the issue &8[&4*&8]").color(),
                    TextMessage.build("&4Found syntax error &8-> &7[&r " + error + "&7 ]").color()
            ));
        } else {
            commandSender.sendMessage(TextMessage.build("&4Found syntax error &8-> &7[&r " + error + "&7 ]").color().getText());
        }
    }

    public static void sqlSearch(final Integer flags, final CommandSender sender, final String name) {
        if (flags == null || flags == -1) {
            sender.sendMessage(TextMessage.build("&8» &4Player &f" + name + " &4not found!").color().getText());
        } else {
            sender.sendMessage(TextMessage.build("&8» &ePlayer &f" + name + " &ehas &6" + flags + " &eflags.").color().getText());
        }
    }

    public static void topSearch(final CommandSender sender, final String[] args, final Integer search, final List<SQLiteUtils.Pair<String, Integer>> result) {
        sender.sendMessage(TextMessage.build("&7---------------------------------").color().getText());
        result.forEach(pair -> {
            sender.sendMessage(TextMessage.build("&7|  &e" + pair.getX() + "  &6" + pair.getY()).color().getText());
        });
        sender.sendMessage(TextMessage.build("&7---------------------------------").color().getText());
    }

    public static void processCommandInput(final String[] args, final CommandSender sender, final File dataFolder) {
        if (sender.hasPermission(SPermissions.COMMAND.getConcatPermission("main"))) {
            final int length = args.length;
            if (length == 0) {
                noArguments(sender);
            } else if (args[0].equalsIgnoreCase("sql")) {
                if (length >= 3) {
                    if (args[1].equalsIgnoreCase("search") && length == 3) {
                        final String playerName = args[2];
                        SQLiteUtils.getPlayerData(dataFolder, playerName, e -> {
                            sender.sendMessage(TextMessage.build("&4Something went wrong when trying to obtain data for " + playerName, "Check your console!").color().getText());
                            e.printStackTrace();
                        }).thenAcceptAsync(pair -> sqlSearch(pair.getY(), sender, playerName));
                    } else if (args[1].equalsIgnoreCase("top") && args[2].matches("[0-9]+") && length == 3) {
                        final int search = Integer.parseInt(args[2]);
                        //TODO: Implement
                    } else {
                        sendWarning(sender, "'&7" + args[1] + "&f' is an invalid argument");
                    }
                } else {
                    sendWarning(sender, "Invalid args number");
                }
            }
        }
    }

}
