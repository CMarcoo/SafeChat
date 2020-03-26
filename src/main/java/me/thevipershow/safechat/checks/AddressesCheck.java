package me.thevipershow.safechat.checks;

import me.thevipershow.safechat.config.EnumConfig;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@SuppressWarnings("UnstableApiUsage")
public final class AddressesCheck implements ChatCheck {

    @Override
    public void result(final String message, final AsyncPlayerChatEvent chatEvent) {

        final String stringToCheck = message.replaceAll(EnumConfig.IPV4_WHITELIST.getString(), "");
        boolean result = stringToCheck.matches(EnumConfig.IPV4_REGEX.getString());

        if (result && !chatEvent.isCancelled()) {
            chatEvent.setCancelled(true);
            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(EnumConfig.IPV4_WARNING.getStringList().toArray(String[]::new)).color(),
                    TextMessage.build(EnumConfig.IPV4_HOVER.getStringList().toArray(String[]::new)).color()
            ));
        }
    }
}
