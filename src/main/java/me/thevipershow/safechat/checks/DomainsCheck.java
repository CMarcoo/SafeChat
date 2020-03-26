package me.thevipershow.safechat.checks;

import me.thevipershow.safechat.config.EnumConfig;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class DomainsCheck implements ChatCheck {

    @Override
    public void result(final String message, final AsyncPlayerChatEvent chatEvent) {

        final String stringToCheck = message.replaceAll(EnumConfig.DOMAIN_WHITELIST.getString(), "");
        boolean result = stringToCheck.matches(EnumConfig.DOMAIN_REGEX.getString());

        if (result && !chatEvent.isCancelled()) {
            chatEvent.setCancelled(true);
            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(EnumConfig.DOMAIN_WARNING.getStringList().toArray(String[]::new)).color(),
                    TextMessage.build(EnumConfig.DOMAIN_HOVER.getStringList().toArray(String[]::new)).color()
            ));
        }
    }
}
