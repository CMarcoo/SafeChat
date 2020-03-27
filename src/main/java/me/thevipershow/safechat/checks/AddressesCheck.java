package me.thevipershow.safechat.checks;

import me.thevipershow.safechat.enums.EnumConfig;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import me.thevipershow.spigotchatlib.chat.builders.HoverMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("UnstableApiUsage")
public final class AddressesCheck implements ChatCheck {

    @Override
    public void result(final String message, final AsyncPlayerChatEvent chatEvent, final Plugin plugin) {

        final String stringToCheck = message.replaceAll(EnumConfig.IPV4_WHITELIST.getString(), "");
        boolean result = stringToCheck.matches(EnumConfig.IPV4_REGEX.getString());

        if (result && !chatEvent.isCancelled()) {
            chatEvent.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new FlagThrownEvent(1, "addresses", chatEvent.getPlayer().getUniqueId()));
            chatEvent.getPlayer().spigot().sendMessage(HoverMessageBuilder.buildHover(
                    TextMessage.build(EnumConfig.IPV4_WARNING.getStringList().toArray(String[]::new)).color(),
                    TextMessage.build(EnumConfig.IPV4_HOVER.getStringList().toArray(String[]::new)).color()
            ));
        }
    }
}
