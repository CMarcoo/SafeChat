package me.thevipershow.safechat;

import lombok.Getter;
import me.thevipershow.safechat.api.config.ConfigManager;
import me.thevipershow.safechat.api.config.TomlConfigurations;
import me.thevipershow.safechat.api.sql.ISafeChatDB;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SafeChatPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private ISafeChatDB iSafeChatDB;

    private ISafeChatDB loadDatabase() {
        // TODO: finish coding UwU
        return null;
    }

    @Override
    public final void onEnable() {
        configManager = ConfigManager.getInstance(this);
        configManager.loadAllConfigs(TomlConfigurations.class);
    }

    @Override
    public final void onDisable() {

    }
}
