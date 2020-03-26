package me.thevipershow.safechat.config;

import me.thevipershow.safechat.Safechat;
import org.bukkit.plugin.Plugin;

import java.util.List;

public enum EnumConfig {

    USERNAME("database.username"),
    PASSWORD("database.password"),
    PORT("database.port"),
    ADDRESS("database.address"),
    DATABASE("database.database"),
    TABLE("database.table"),
    DOMAIN_REGEX("domains.regex"),
    DOMAIN_WHITELIST("domains.whitelisted"),
    DOMAIN_WARNING("domains.warning"),
    DOMAIN_HOVER("domains.hover-warning"),
    IPV4_REGEX("addresses.regex"),
    IPV4_WHITELIST("addresses.whitelisted"),
    IPV4_WARNING("addresses.warning"),
    IPV4_HOVER("addresses.hover-warning");

    private String value;

    public final String getString() {
        return Safechat.getPlugin(Safechat.class).getConfig().getString("safechat.".concat(this.value));
    }

    public final int getInt() {
        return Safechat.getPlugin(Safechat.class).getConfig().getInt("safechat.".concat(this.value));
    }

    public final List<String> getStringList() {
        return Safechat.getPlugin(Safechat.class).getConfig().getStringList("safechat.".concat(this.value));
    }

    EnumConfig(String value) {
        this.value = value;
    }


}
