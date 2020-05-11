package me.thevipershow.safechat.enums;

import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;

public enum EnumConfig {
    SERIAL_UID("serialUID"),
    ENABLED("safechat.database.enabled"),
    USERNAME("safechat.database.username"),
    PASSWORD("safechat.database.password"),
    PORT("safechat.database.port"),
    ADDRESS("safechat.database.address"),
    DATABASE("safechat.database.database"),
    DOMAIN_REGEX("safechat.domains.regex"),
    DOMAIN_WHITELIST("safechat.domains.whitelisted"),
    DOMAIN_WARNING("safechat.domains.warning"),
    DOMAIN_HOVER("safechat.domains.hover-warning"),
    IPV4_REGEX("safechat.addresses.regex"),
    IPV4_WHITELIST("safechat.addresses.whitelisted"),
    IPV4_WARNING("safechat.addresses.warning"),
    IPV4_HOVER("safechat.addresses.hover-warning");

    private final String value;

    public final String getString(FileConfiguration configuration) {
        return configuration.getString(value);
    }

    public final int getInt(FileConfiguration configuration) {
        return configuration.getInt(value);
    }

    public final boolean getBool(FileConfiguration configuration) {
        return configuration.getBoolean(value);
    }

    public final List<String> getStringList(FileConfiguration configuration) {
        return configuration.getStringList(value);
    }

    EnumConfig(String value) {
        this.value = value;
    }
}

