package me.thevipershow.safechat.checks.register;

import me.thevipershow.safechat.checks.AddressesCheck;
import me.thevipershow.safechat.checks.ChatCheck;
import me.thevipershow.safechat.checks.DomainsCheck;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CheckRegister implements Listener {

    private final static ArrayList<ChatCheck> chatChecks = new ArrayList<>(Arrays.asList(
            new AddressesCheck(),
            new DomainsCheck()));

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {

        chatChecks.forEach(c -> c.result(event.getMessage(), event));
    }
}
