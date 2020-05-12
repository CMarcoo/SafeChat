/*
 * The MIT License
 *
 * Copyright 2020 marco.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.thevipershow.safechat.config;

import java.util.List;
import me.thevipershow.safechat.enums.EnumConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Values {

    private final JavaPlugin plugin;

    private Values(JavaPlugin plugin) {
        this.plugin = plugin;
        updateAll();
    }

    private static Values instance = null;

    public static Values getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new Values(plugin);
        }
        return instance;
    }

    public final void updateAll() {
        plugin.reloadConfig();
        final FileConfiguration configuration = plugin.getConfig();
        this.serialUID = EnumConfig.SERIAL_UID.getInt(configuration);
        this.dbType = EnumConfig.DB_TYPE.getString(configuration);
        this.enabled = EnumConfig.ENABLED.getBool(configuration);
        this.username = EnumConfig.USERNAME.getString(configuration);
        this.password = EnumConfig.PASSWORD.getString(configuration);
        this.port = EnumConfig.PORT.getInt(configuration);
        this.address = EnumConfig.ADDRESS.getString(configuration);
        this.database = EnumConfig.DATABASE.getString(configuration);
        this.domainRegex = EnumConfig.DOMAIN_REGEX.getString(configuration);
        this.domainWhitelist = EnumConfig.DOMAIN_WHITELIST.getString(configuration);
        this.domainWarning = EnumConfig.DOMAIN_WARNING.getStringList(configuration);
        this.domainHover = EnumConfig.DOMAIN_HOVER.getStringList(configuration);
        this.ipv4Regex = EnumConfig.IPV4_REGEX.getString(configuration);
        this.ipv4Whitelist = EnumConfig.IPV4_WHITELIST.getString(configuration);
        this.ipv4Warning = EnumConfig.IPV4_WARNING.getStringList(configuration);
        this.ipv4Hover = EnumConfig.IPV4_HOVER.getStringList(configuration);
    }

    private int serialUID;
    private String dbType;
    private boolean enabled;
    private String username;
    private String password;
    private int port;
    private String address;
    private String database;
    private String domainRegex;
    private String domainWhitelist;
    private List<String> domainWarning;
    private List<String> domainHover;
    private String ipv4Regex;
    private String ipv4Whitelist;
    private List<String> ipv4Warning;
    private List<String> ipv4Hover;

    public final int getSerialUID() {
        return serialUID;
    }

    public String getDbType() {
        return dbType;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final String getUsername() {
        return username;
    }

    public final String getPassword() {
        return password;
    }

    public final int getPort() {
        return port;
    }

    public final String getAddress() {
        return address;
    }

    public final String getDatabase() {
        return database;
    }

    public final String getDomainRegex() {
        return domainRegex;
    }

    public final String getDomainWhitelist() {
        return domainWhitelist;
    }

    public final List<String> getDomainWarning() {
        return domainWarning;
    }

    public final List<String> getDomainHover() {
        return domainHover;
    }

    public final String getIpv4Regex() {
        return ipv4Regex;
    }

    public final String getIpv4Whitelist() {
        return ipv4Whitelist;
    }

    public final List<String> getIpv4Warning() {
        return ipv4Warning;
    }

    public final List<String> getIpv4Hover() {
        return ipv4Hover;
    }
}
