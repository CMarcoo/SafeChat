package me.thevipershow.safechat.commands;

import com.zaxxer.hikari.HikariDataSource;
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
import org.bukkit.plugin.Plugin;

public class SafeChatCommand implements CommandExecutor {

    private final Plugin plugin;
    private final HikariDataSource dataSource;
    private final boolean isOnlineMode;

    public SafeChatCommand(Plugin plugin, HikariDataSource dataSource, boolean isOnlineMode) {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.isOnlineMode = isOnlineMode;
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

                        PostgreSQLUtils.getPlayerScore(dataSource, args[2], isOnlineMode).thenAccept(integer -> {
                            if (integer == -1 || integer == null) {
                                sender.sendMessage(TextMessage.build("&8» &4Player &f" + args[2] + " &4not found!").color().getText());
                            } else {
                                sender.sendMessage(TextMessage.build("&8» &ePlayer &f" + args[2] + " &ehas &6" + integer + " &eflags.").color().getText());
                            }
                        });

                    } else if (args[1].equalsIgnoreCase("top") && args[2].matches("[0-9]+") && length == 3) {

                        sender.sendMessage(TextMessage.build("&7---------------------------------").color().getText());
                        PostgreSQLUtils.getTopData(dataSource, Integer.parseInt(args[2])).thenAccept(r -> {
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
