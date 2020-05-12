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
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.enums.HoverMessages;
import me.thevipershow.safechat.enums.SPermissions;
import me.thevipershow.safechat.sql.PostgreSQLUtils;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SafeChatCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final HikariDataSource dataSource;
    private final boolean isOnlineMode;
    private final Values values;

    public SafeChatCommand(JavaPlugin plugin, HikariDataSource dataSource, boolean isOnlineMode) {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.isOnlineMode = isOnlineMode;
        this.values = Values.getInstance(plugin);
    }

    private void noArguments(final CommandSender commandSender) {
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

    private void sendWarning(final CommandSender commandSender, final String error) {
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

        if (sender.hasPermission(SPermissions.COMMAND.getConcatPermission("main"))) {
            final int length = args.length;
            if (length == 0) {
                noArguments(sender);
            } else if (args[0].equalsIgnoreCase("sql")) {
                if (length >= 3) {
                    if (args[1].equalsIgnoreCase("search") && length == 3) {

                        PostgreSQLUtils.getPlayerScore(dataSource, args[2], isOnlineMode, values.getTable()).thenAccept(integer -> {
                            if (integer == -1 || integer == null) {
                                sender.sendMessage(TextMessage.build("&8» &4Player &f" + args[2] + " &4not found!").color().getText());
                            } else {
                                sender.sendMessage(TextMessage.build("&8» &ePlayer &f" + args[2] + " &ehas &6" + integer + " &eflags.").color().getText());
                            }
                        });

                    } else if (args[1].equalsIgnoreCase("top") && args[2].matches("[0-9]+") && length == 3) {

                        sender.sendMessage(TextMessage.build("&7---------------------------------").color().getText());
                        PostgreSQLUtils.getTopData(dataSource, Integer.parseInt(args[2]), values.getTable()).thenAccept(r -> {
                            r.forEach((uuid, integer) -> sender.sendMessage(TextMessage.build("&7|  &e" + Bukkit.getOfflinePlayer(uuid).getName() + "  &6" + integer).color().getText()));
                            sender.sendMessage(TextMessage.build("&7---------------------------------").color().getText());
                        });

                    } else {
                        sendWarning(sender, "'&7" + args[1] + "&f' is an invalid argument");
                    }
                } else {
                    sendWarning(sender, "Invalid args number");
                }
            }
        }
        return true;
    }
}
