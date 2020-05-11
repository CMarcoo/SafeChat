package me.thevipershow.safechat.checks.register;

import me.thevipershow.safechat.checks.AddressesCheck;
import me.thevipershow.safechat.checks.ChatCheck;
import me.thevipershow.safechat.checks.DomainsCheck;
import me.thevipershow.safechat.enums.SPermissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class CheckRegister implements Listener {

    private static CheckRegister instance = null;
    private final JavaPlugin plugin;

    private CheckRegister(JavaPlugin plugin) {
        this.domainsCheck = DomainsCheck.getInstance(plugin);
        this.addressesCheck = AddressesCheck.getInstance(plugin);
        this.chatChecks = new ChatCheck[]{addressesCheck, domainsCheck};
        this.plugin = plugin;
    }

    public static CheckRegister getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new CheckRegister(plugin);
        }
        return instance;
    }

    final AddressesCheck addressesCheck;
    final DomainsCheck domainsCheck;
    final ChatCheck[] chatChecks;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission(SPermissions.BYPASS.getPermission())) {
            for (ChatCheck check : chatChecks) {
                check.result(event.getMessage(), event, this.plugin);
            }
        }
    }

}
