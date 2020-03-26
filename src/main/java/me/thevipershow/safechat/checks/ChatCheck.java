package me.thevipershow.safechat.checks;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface ChatCheck {
    void result(final String message, final AsyncPlayerChatEvent chatEvent);
}
