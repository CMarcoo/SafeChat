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
            LiteralCommandNode<?> safechatCommand = CommodoreFileFormat.parse(plugin.getResource("safechat.commodore"));
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
                    TextMessage.build("&7Â» &4Your command was incorrect!", "&7Hover here to see the issue &8[&4*&8]").color(),
                    TextMessage.build("&4Found syntax error &8-> &7[&r " + error + "&7 ]").color()
            ));
        } else {
            commandSender.sendMessage(TextMessage.build("&4Found syntax error &8-> &7[&r " + error + "&7 ]").color().getText());
        }
    }

    private static void sqlSearch(final DataManager dataManager, final CommandSender sender, final String searchName) {
        for (Map.Entry<UUID, PlayerData> entry : dataManager.getPlayerData().entrySet()) {
            final PlayerData playerData = entry.getValue();
            if (playerData.getUsername().equals(searchName)) {
                sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: &8» &ePlayer " + searchName + " &fflags list:",
                        "  &8[&6Domains Check&8]&7: &e" + playerData.getDomainFlags() + " &fflags",
                        "  &8[&6IPv4 Check&8]&7: &e" + playerData.getIpv4Flags() + " &fflags",
                        "  &8[&6Words Check&8]&7: &e" + playerData.getDomainFlags() + " &fflags").color().getText());
                return;
            }
        }
        sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: Player " + searchName + " was not found in the database!").color().getText());
    }

    private static void topSearch(final CommandSender sender, final DataManager dataManager, final int top, final CheckName checkName) {
        final List<PlayerData> playerDataList = dataManager.getTopCheckData(checkName, top);
        if (playerDataList != null) {
            if (!playerDataList.isEmpty()) {
                sender.sendMessage(TextMessage.build("&7---------------------------------").color().getText());
                for (PlayerData playerData : playerDataList) {
                    sender.sendMessage(TextMessage.build("&7| &e" + playerData.getUsername() + "  &7|  &e" + playerData.getFlag(checkName)).color().getText());
                }
            } else {
                sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7: No data was found").color().getText());
            }
        } else {
            sender.sendMessage(TextMessage.build("&8[&6SafeChat&8]&7:  &cSomething went wrong when loading data!").color().getText());
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
                if (length >= 3) {
                    if (args[1].equalsIgnoreCase("search") && length == 3) {
                        final String playerName = args[2];
                        sqlSearch(dataManager, sender, playerName);


                    } else if (args[1].equalsIgnoreCase("top") && args[2].matches("[0-9]+") && length == 3) {
                        final int search = Integer.parseInt(args[2]);


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
