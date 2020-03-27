package me.thevipershow.safechat.checks.register;

import me.thevipershow.safechat.checks.AddressesCheck;
import me.thevipershow.safechat.checks.ChatCheck;
import me.thevipershow.safechat.checks.DomainsCheck;
import me.thevipershow.safechat.enums.SPermissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

public final class CheckRegister implements Listener {

    private final Plugin plugin;

    private final static ArrayList<ChatCheck> chatChecks = new ArrayList<>(Arrays.asList(
            new AddressesCheck(),
            new DomainsCheck()));

    public CheckRegister(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {

        if (!event.getPlayer().hasPermission(SPermissions.BYPASS.getPermission())) {
            chatChecks.forEach(c -> c.result(event.getMessage(), event, plugin));
        }

    }

}
