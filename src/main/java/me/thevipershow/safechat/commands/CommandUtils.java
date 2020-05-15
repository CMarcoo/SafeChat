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

import java.util.Map;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.enums.HoverMessages;
import me.thevipershow.safechat.enums.SPermissions;
import me.thevipershow.safechat.sql.DatabaseManager;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author marco
 */
public final class CommandUtils {

    private static void noArguments(final CommandSender commandSender) {
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

    private static void sendWarning(final CommandSender commandSender, final String error) {
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

    private static void sqlSearch(final Integer flags, final CommandSender sender, final String name) {
        if (flags == null) {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &8» &4Player &f" + name + " &4not found!").color().getText());
        } else {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &8» &ePlayer &f" + name + " &ehas &6" + flags + " &eflags.").color().getText());
        }
    }

    private static void topSearch(final CommandSender sender, final Map<String, Integer> result) {
        if (result != null) {
            if (!result.isEmpty()) {
                sender.sendMessage(TextMessage.build("&7---------------------------------").color().getText());
                result.forEach((name, flags) -> {
                    sender.sendMessage(TextMessage.build("&7|  &e" + name + "  &6 " + flags).color().getText());
                    sender.sendMessage(TextMessage.build("&7---------------------------------").color().getText());
                });
            } else {
                sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: No data was found").color().getText());
            }
        } else {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7:  &cSomething went wrong when loading data!").color().getText());
        }
    }

    public static void processCommand(final DatabaseManager databaseManager, final String[] args, final CommandSender sender, Values values) {
        if (sender.hasPermission(SPermissions.COMMAND.getConcatPermission("main"))) {
            final int length = args.length;
            if (length == 0) {
                noArguments(sender);
            } else if (args[0].equalsIgnoreCase("reload") && length == 1) {
                values.updateAll();
                sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &aSuccessfully reloaded the config.yml values").color().getText());
            } else if (args[0].equalsIgnoreCase("sql")) {
                if (length >= 3) {
                    if (args[1].equalsIgnoreCase("search") && length == 3) {
                        final String playerName = args[2];
                        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                        if (!offlinePlayer.hasPlayedBefore()) {
                            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &cThe player you specified has never joined this server!").color().getText());
                        } else {
                            databaseManager.getPlayerData(offlinePlayer.getUniqueId())
                                    .thenAcceptAsync(i -> sqlSearch(i, sender, playerName))
                                    .exceptionally(i -> {
                                        i.printStackTrace();
                                        return null;
                                    });
                        }

                    } else if (args[1].equalsIgnoreCase("top") && args[2].matches("[0-9]+") && length == 3) {
                        final int search = Integer.parseInt(args[2]);
                        databaseManager.getTopData(search).thenAcceptAsync(data -> topSearch(sender, data)).exceptionally(i -> {
                            i.printStackTrace();
                            return null;
                        });
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
