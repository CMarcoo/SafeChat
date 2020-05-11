package me.thevipershow.safechat.checks;

import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class DomainsCheck implements ChatCheck {

    private final JavaPlugin plugin;

    private static DomainsCheck instance = null;

    private DomainsCheck(JavaPlugin plugin) {
        this.plugin = plugin;
        values = Values.getInstance(plugin);
    }

    public static DomainsCheck getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new DomainsCheck(plugin);
        }
        return instance;
    }

    private final Values values;

    @Override
    public void result(final String message, final AsyncPlayerChatEvent chatEvent, final Plugin plugin) {

        final String stringToCheck = message.replaceAll(values.getDomainWhitelist(), "");
        boolean result = stringToCheck.matches(values.getDomainRegex());

        if (result && !chatEvent.isCancelled()) {
            chatEvent.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new FlagThrownEvent(1, "domains", chatEvent.getPlayer().getUniqueId()));
            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(values.getDomainWarning().toArray(String[]::new)).color(),
                    TextMessage.build(values.getDomainHover().toArray(String[]::new)).color()
            ));
        }
    }
}

