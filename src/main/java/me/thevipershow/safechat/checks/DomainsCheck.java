package me.thevipershow.safechat.checks;

import me.thevipershow.safechat.enums.EnumConfig;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public final class DomainsCheck implements ChatCheck {

    @Override
    public void result(final String message, final AsyncPlayerChatEvent chatEvent, final Plugin plugin) {

        final String stringToCheck = message.replaceAll(EnumConfig.DOMAIN_WHITELIST.getString(), "");
        boolean result = stringToCheck.matches(EnumConfig.DOMAIN_REGEX.getString());

        if (result && !chatEvent.isCancelled()) {
            chatEvent.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new FlagThrownEvent(1, "domains", chatEvent.getPlayer().getUniqueId()));
            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(EnumConfig.DOMAIN_WARNING.getStringList().toArray(String[]::new)).color(),
                    TextMessage.build(EnumConfig.DOMAIN_HOVER.getStringList().toArray(String[]::new)).color()
            ));
        }
    }
}
