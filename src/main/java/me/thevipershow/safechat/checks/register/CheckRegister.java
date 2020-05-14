package me.thevipershow.safechat.checks.register;

import java.util.HashSet;
import java.util.Set;
import me.thevipershow.safechat.checks.AddressesCheck;
import me.thevipershow.safechat.checks.ChatCheck;
import me.thevipershow.safechat.checks.DomainsCheck;
import me.thevipershow.safechat.checks.WordsCheck;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.enums.SPermissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class CheckRegister implements Listener {

    private static CheckRegister instance = null;

    private final Set<ChatCheck> chatChecks = new HashSet<>();
    private final Values values;

    private CheckRegister(final Values values) {
        this.values = values;
        if (instance == null) {
            update();
        }
    }

    public final void update() {
        chatChecks.clear();
        if (values.isWordsEnabled()) {
            chatChecks.add(WordsCheck.getInstance(values));
        }
        if (values.isIpv4Enabled()) {
            chatChecks.add(AddressesCheck.getInstance(values));
        }
        if (values.isDomainEnabled()) {
            chatChecks.add(DomainsCheck.getInstance(values));
        }
    }

    public final boolean addCheck(ChatCheck chatCheck) {
        return chatChecks.add(chatCheck);
    }

    public final boolean removeCheck(ChatCheck chatCheck) {
        return chatChecks.remove(chatCheck);
    }

    public static CheckRegister getInstance(Values values) {
        return instance != null ? instance : (instance = new CheckRegister(values));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission(SPermissions.BYPASS.getPermission())) {
            chatChecks.forEach(check -> check.result(event.getMessage(), event));
        }
    }
}
