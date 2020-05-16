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

import com.mojang.brigadier.tree.LiteralCommandNode;
import java.io.IOException;
import java.util.*;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.file.CommodoreFileFormat;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.enums.CheckName;
import me.thevipershow.safechat.enums.HoverMessages;
import me.thevipershow.safechat.enums.SPermissions;
import me.thevipershow.safechat.sql.DataManager;
import me.thevipershow.safechat.sql.ExceptionHandler;
import me.thevipershow.safechat.sql.PlayerData;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author marco
 */
public final class CommandUtils {

    public static void registerCompletions(Commodore commodore, PluginCommand command, JavaPlugin plugin, ExceptionHandler handler) {
        try {
            LiteralCommandNode<?> safechatCommand = CommodoreFileFormat.parse(Objects.requireNonNull(plugin.getResource("safechat.commodore")));
            commodore.register(safechatCommand);
        } catch (IOException e) {
            handler.handle(e);
        }
    }

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
                    TextMessage.build("&7» &4Your command was incorrect!", "&7Hover here to see the issue &8[&4*&8]").color(),
                    TextMessage.build("&4Found syntax error &8-> &7[&r " + error + "&7 ]").color()
            ));
        } else {
            commandSender.sendMessage(TextMessage.build("&4Found syntax error &8-> &7[&r " + error + "&7 ]").color().getText());
        }
    }

    private static void sqlSearch(final DataManager dataManager, final CommandSender sender, final String searchName) {
        final List<EnumMap<CheckName, Integer>> obtained = dataManager.getPlayerFlags(searchName);
        if (obtained != null) {
            obtained.forEach(enumMap -> {
                sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &8» &ePlayer " + searchName + " &fflags list:",
                        "  &8[&6Domains Check&8]&7: &e" + enumMap.get(CheckName.DOMAINS) + " &fflags",
                        "  &8[&6IPv4 Check&8]&7: &e" + enumMap.get(CheckName.ADDRESSES) + " &fflags",
                        "  &8[&6Words Check&8]&7: &e" + enumMap.get(CheckName.WORDS) + " &fflags").color().getText());
            });
        } else {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: Player " + searchName + " was not found in the database!").color().getText());
        }
    }

    private static void sqlSearch(final DataManager dataManager, final CommandSender sender, final String searchName, final CheckName checkName) {
        final List<Integer> obtained = dataManager.getPlayerFlags(searchName, checkName);
        if (obtained != null) {
            obtained.forEach(n -> {
                sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &8» &ePlayer &f" + searchName + " &7has &e" + n + " &7" + checkName.name() + " flags").color().getText());
            });
        } else {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: Player " + searchName + " was not found in the database!").color().getText());
        }
    }

    private static void topSearch(final CommandSender sender, final DataManager dataManager, final int top, final CheckName checkName) {
        final List<PlayerData> playerDataList = dataManager.getTopCheckData(checkName, top);
        if (playerDataList != null) {
            if (!playerDataList.isEmpty()) {
                sender.sendMessage(TextMessage.build("&7---------------------------------").color().getText());
                for (final PlayerData playerData : playerDataList) {
                    sender.sendMessage(TextMessage.build("&7| &e" + playerData.getUsername() + "  &7|  &e" + playerData.getFlag(checkName)).color().getText());
                }
            } else {
                sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: No data was found").color().getText());
            }
        } else {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7:  &cThe provided number was too huge (1-250)!").color().getText());
        }
    }

    public static void processCommand(final DataManager dataManager, final String[] args, final CommandSender sender, Values values) {
        if (sender.hasPermission(SPermissions.COMMAND.getConcatPermission("main"))) {
            final int length = args.length;
            if (length == 0) {
                noArguments(sender);
            } else if (args[0].equalsIgnoreCase("reload") && length == 1) {
                values.updateAll();
                sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &aSuccessfully reloaded the config.yml values").color().getText());
            } else if (args[0].equalsIgnoreCase("sql")) {
                if (length == 3) {
                    if (args[1].equalsIgnoreCase("search")) {
                        final String playerName = args[2];
                        sqlSearch(dataManager, sender, playerName);
                    } else {
                        sendWarning(sender, "'&7" + args[1] + "&f' is an invalid argument");
                    }
                } else if (length == 4) {
                    if (args[1].equalsIgnoreCase("search")) {
                        final String playerName = args[3];
                        final String checkType = args[2].toLowerCase(Locale.getDefault());
                        switch (checkType) {
                            case "words":
                                sqlSearch(dataManager, sender, playerName, CheckName.WORDS);
                                break;
                            case "domains":
                                sqlSearch(dataManager, sender, playerName, CheckName.DOMAINS);
                                break;
                            case "ipv4":
                                sqlSearch(dataManager, sender, playerName, CheckName.ADDRESSES);
                                break;
                            default:
                                sendWarning(sender, "`&7" + checkType + "&f` is an invalid check type");
                                break;
                        }
                    } else if (args[1].equalsIgnoreCase("top")) {
                        if (args[2].matches("[0-9]+]")) {
                            int search = Integer.parseInt(args[2]);
                            final String checkType = args[3];
                            switch (checkType) {
                                case "words":
                                    topSearch(sender, dataManager, search, CheckName.WORDS);
                                    break;
                                case "domains":
                                    topSearch(sender, dataManager, search, CheckName.DOMAINS);
                                    break;
                                case "ipv4":
                                    topSearch(sender, dataManager, search, CheckName.ADDRESSES);
                                    break;
                                default:
                                    sendWarning(sender, "`&7" + checkType + "&f` is an invalid check type");
                                    break;
                            }
                        } else {
                            sendWarning(sender, "'&7" + args[2] + "&f' is not a number!");
                        }
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
