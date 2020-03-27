package me.thevipershow.safechat.checks;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

public interface ChatCheck {
    void result(final String message, final AsyncPlayerChatEvent chatEvent, final Plugin plugin);
}
