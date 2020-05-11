package me.thevipershow.safechat.checks;

import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.enums.EnumConfig;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("UnstableApiUsage")
public final class AddressesCheck implements ChatCheck {

    private final JavaPlugin plugin;
    private static AddressesCheck instance = null;

    private AddressesCheck(JavaPlugin plugin) {
        this.values = Values.getInstance(plugin);
        this.plugin = plugin;
    }

    public static AddressesCheck getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new AddressesCheck(plugin);
        }
        return instance;
    }

    private final Values values;

    @Override
    public void result(final String message, final AsyncPlayerChatEvent chatEvent, final Plugin plugin) {

        final String stringToCheck = message.replaceAll(values.getIpv4Whitelist(), "");
        boolean result = stringToCheck.matches(values.getIpv4Regex());

        if (result && !chatEvent.isCancelled()) {
            chatEvent.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new FlagThrownEvent(1, "addresses", chatEvent.getPlayer().getUniqueId()));
            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(values.getIpv4Warning().toArray(String[]::new)).color(),
                    TextMessage.build(values.getIpv4Hover().toArray(String[]::new)).color()
            ));
        }
    }
}

