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

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.EnumMap;
import java.util.List;
import me.lucko.commodore.Commodore;
import me.thevipershow.safechat.common.config.AbstractValues;
import me.thevipershow.safechat.common.enums.CheckName;
import me.thevipershow.safechat.common.enums.HoverMessages;
import me.thevipershow.safechat.common.enums.SPermissions;
import static me.thevipershow.safechat.common.enums.SPermissions.*;
import me.thevipershow.safechat.common.sql.DataManager;
import me.thevipershow.safechat.common.sql.ExceptionHandler;
import me.thevipershow.safechat.common.sql.PlayerData;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author marco
 */
public final class CommandUtils {

    public static void registerCompletions(final Commodore commodore, final JavaPlugin plugin, final ExceptionHandler handler) {
        final PluginCommand safechatCommand = plugin.getCommand("safechat");
        final LiteralArgumentBuilder<?> safechat = LiteralArgumentBuilder.literal("safechat");

        final LiteralCommandNode<?> reload = safechat.then(LiteralArgumentBuilder.literal("reload")).build();
        commodore.register(safechatCommand, reload, RELOAD.toPredicate());

        final LiteralCommandNode<?> command = safechat.then(LiteralArgumentBuilder.literal("top"))
                .then(RequiredArgumentBuilder.argument("count", IntegerArgumentType.integer(1, 250)))
                .then(LiteralArgumentBuilder.literal("ipv4"))
                .then(LiteralArgumentBuilder.literal("domains"))
                .then(LiteralArgumentBuilder.literal("words")).then(LiteralArgumentBuilder.literal("search"))
                .then(LiteralArgumentBuilder.literal("ipv4"))
                .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word()))
                .then(LiteralArgumentBuilder.literal("words"))
                .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word()))
                .then(LiteralArgumentBuilder.literal("domains"))
                .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word()))
                .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word())).build();

        commodore.register(safechatCommand, command, COMMAND.toPredicate());
               /* try {
                    LiteralCommandNode<?> node = CommodoreFileFormat.parse(plugin.getResource("safechat.commodore"));
                    commodore.register(safechatCommand, node, COMMAND.toPredicate());
                } catch (IOException e) {
                    handler.handle(e);
                }
                */
    }

    private static void permissionCheck(final CommandSender sender, final SPermissions permission, final Action action) {
        if (sender.hasPermission(permission.getPermission())) {
            action.perform();
        } else {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &cYou are missing permissions").color().getText());
        }
    }

    private static void clearPlayer(final String username, final CommandSender sender, final DataManager dataManager) {
        final int removed = dataManager.removeAllPlayer(username);
        if (removed == 0) {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &7No player with name &e" + username + " &7could be found!").color().getText());
        } else {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &7Found &e" + removed + " &7players with name &e" + username + " &7and cleaned their data!").color().getText());
        }
    }

    private static void clearPlayer(final String username, final CommandSender sender, final DataManager dataManager, final CheckName checkName) {
        final int removed = dataManager.removeAllPlayer(username, checkName);
        if (removed == 0) {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &7No player with name &e" + username + " &7could be found!").color().getText());
        } else {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &7Found &e" + removed + " &7players with name &e" + username + " &7and cleaned their data!").color().getText());
        }
    }

    private static void noArguments(final CommandSender commandSender) {
        commandSender.sendMessage(TextMessage.build(HoverMessages.NO_ARGS.getMessages()).color().getText());
    }

    private static void sendWarning(final CommandSender commandSender, final String error) {
        if (commandSender instanceof Player) {
            final Player player = (Player) commandSender;
            player.spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build("&7> &4Your command was incorrect!", "&7Hover here to see the issue &8[&4*&8]").color(),
                    TextMessage.build("&4Found syntax error &8-> &7[&r " + error + "&7 ]").color()
            ));
        } else {
            commandSender.sendMessage(TextMessage.build("&4Found syntax error &8-> &7[&r " + error + "&7 ]").color().getText());
        }
    }

    private static void sqlSearch(final DataManager dataManager, final CommandSender sender, final String searchName) {
        final List<EnumMap<CheckName, Integer>> obtained = dataManager.getPlayerFlags(searchName);
        if (obtained != null) {
            obtained.forEach(enumMap -> sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &8» &6Player " + searchName + " &fflags list:",
                    "  &8[&6Domains Check&8]&7: &6" + enumMap.get(CheckName.DOMAINS) + " &fflags",
                    "  &8[&6IPv4 Check&8]&7: &6" + enumMap.get(CheckName.ADDRESSES) + " &fflags",
                    "  &8[&6Words Check&8]&7: &6" + enumMap.get(CheckName.WORDS) + " &fflags").color().getText()));
        } else {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: Player " + searchName + " was not found in the database!").color().getText());
        }
    }

    private static void sqlSearch(final DataManager dataManager, final CommandSender sender, final String searchName, final CheckName checkName) {
        final List<PlayerData> obtained = dataManager.getPlayerFlags(searchName, checkName);
        if (obtained != null) {
            obtained.forEach(n ->
                    sender.sendMessage(
                            TextMessage
                                    .build("&8[&6SafeChat&8]&7: &8» &6Player &f" + searchName + " &7has &6" + n.getFlag(checkName) + " &7" + checkName.name() + " flags")
                                    .color()
                                    .getText())
            );
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
                    sender.sendMessage(TextMessage.build("&7| &6" + playerData.getUsername() + "  &7|  &6" + playerData.getFlag(checkName)).color().getText());
                }
            } else {
                sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: No data was found").color().getText());
            }
        } else {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7:  &cThe provided number was too huge (1-250)!").color().getText());
        }
    }

    private static void reload(final CommandSender sender, final AbstractValues values) {
        values.updateAll();
        sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &aSuccessfully reloaded the config.yml values").color().getText());
    }

    private static CheckName stringToCheckName(final String s) {
        switch (s.toLowerCase()) {
            case "ipv4":
                return CheckName.ADDRESSES;
            case "domains":
                return CheckName.DOMAINS;
            case "words":
                return CheckName.WORDS;
            default:
                return null;
        }
    }

    public static void processCommand(final DataManager dataManager, final String[] args, final CommandSender sender, AbstractValues values) {
        final int length = args.length;
        if (length == 0) {
            permissionCheck(sender, HELP, () -> noArguments(sender));
        } else {
            if (length <= 3) {
                if (length > 1) {
                    final String arg1 = args[0];
                    if (arg1.equalsIgnoreCase("search")) {
                        if (length == 2) {
                            permissionCheck(sender, SEARCH, () -> sqlSearch(dataManager, sender, args[1]));
                        } else {
                            permissionCheck(sender, SEARCH, () -> {
                                final CheckName checkName = stringToCheckName(args[1]); // Nullable
                                if (checkName != null) {
                                    sqlSearch(dataManager, sender, args[2], checkName);
                                } else {
                                    sendWarning(sender, args[1] + " is an invalid check type!");
                                }
                            });
                        }
                    } else if (arg1.equalsIgnoreCase("top")) {
                        if (length == 3) {
                            permissionCheck(sender, TOP, () -> {
                                final String numberString = args[1];
                                try {
                                    final int intFromString = Integer.parseInt(numberString);
                                    final CheckName checkName = stringToCheckName(args[2]);
                                    if (checkName != null) {
                                        topSearch(sender, dataManager, intFromString, checkName);
                                    } else {
                                        sendWarning(sender, args[2] + " is an invalid check type!");
                                    }
                                } catch (final NumberFormatException formatException) {
                                    sendWarning(sender, "Invalid number (" + numberString + ")!");
                                }
                            });
                        } else {
                            permissionCheck(sender, TOP, () -> sendWarning(sender, "Too few arguments for top command!"));
                        }
                    } else if (arg1.equalsIgnoreCase("clear")) {
                        if (length == 2) {
                            permissionCheck(sender, CLEAR, () -> clearPlayer(args[1], sender, dataManager));
                        } else {
                            permissionCheck(sender, CLEAR, () -> {
                                final CheckName checkName = stringToCheckName(args[2]);
                                if (checkName != null) {
                                    clearPlayer(args[1], sender, dataManager, checkName);
                                } else {
                                    sendWarning(sender, args[2] + " is an invalid check type!");
                                }
                            });
                        }
                    } else {
                        permissionCheck(sender, COMMAND, () -> sendWarning(sender, "Unknown command!"));
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    permissionCheck(sender, RELOAD, () -> reload(sender, values));
                }
            } else {
                permissionCheck(sender, COMMAND, () -> sendWarning(sender, "Invalid arguments number (" + length + ")!"));
            }
        }
    }
}
