/*
 * SafeChat - A Minecraft plugin to keep your chat safe.
 *  Copyright (C) 2020 TheViperShow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.thevipershow.safechat.common.config;

import java.util.ArrayList;
import java.util.List;
import me.thevipershow.safechat.common.checks.register.CheckRegister;
import me.thevipershow.safechat.common.enums.EnumConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Values {

    private final JavaPlugin plugin;

    private Values(final JavaPlugin plugin) {
        this.plugin = plugin;
        updateAll();
    }

    private static Values instance = null;

    public static Values getInstance(final JavaPlugin plugin) {
        return instance != null ? instance : (instance = new Values(plugin));
    }

    public final void updateAll() {
        plugin.reloadConfig();
        final FileConfiguration configuration = plugin.getConfig();
        this.enableConsoleLogging = EnumConfig.ENABLE_CONSOLE_LOGGING.getBool(configuration);
        this.serialUID = EnumConfig.SERIAL_UID.getInt(configuration);
        this.dbType = EnumConfig.DB_TYPE.getString(configuration);
        this.username = EnumConfig.USERNAME.getString(configuration);
        this.password = EnumConfig.PASSWORD.getString(configuration);
        this.port = EnumConfig.PORT.getInt(configuration);
        this.address = EnumConfig.ADDRESS.getString(configuration);
        this.database = EnumConfig.DATABASE.getString(configuration);
        this.table = EnumConfig.TABLE.getString(configuration);
        this.autoSave = EnumConfig.AUTO_SAVE.getInt(configuration);
        this.domainEnabled = EnumConfig.DOMAIN_ENABLED.getBool(configuration);
        this.domainRegex = EnumConfig.DOMAIN_REGEX.getString(configuration);
        this.domainWhitelist = EnumConfig.DOMAIN_WHITELIST.getString(configuration);
        this.domainWarning = EnumConfig.DOMAIN_WARNING.getStringList(configuration);
        this.domainHover = EnumConfig.DOMAIN_HOVER.getStringList(configuration);
        this.domainExecutables = EnumConfig.DOMAIN_EXECUTABLES.getExecutableObject(configuration);
        this.ipv4Enabled = EnumConfig.IPV4_ENABLED.getBool(configuration);
        this.ipv4Regex = EnumConfig.IPV4_REGEX.getString(configuration);
        this.ipv4Whitelist = EnumConfig.IPV4_WHITELIST.getString(configuration);
        this.ipv4Warning = EnumConfig.IPV4_WARNING.getStringList(configuration);
        this.ipv4Hover = EnumConfig.IPV4_HOVER.getStringList(configuration);
        this.ipv4Executables = EnumConfig.IPV4_EXECUTABLES.getExecutableObject(configuration);
        this.wordsEnabled = EnumConfig.WORDS_ENABLED.getBool(configuration);
        this.blacklistWords = EnumConfig.WORDS_BLACKLIST.getWordsMatcherList(configuration);
        this.wordsWarning = EnumConfig.WORDS_WARNING.getStringList(configuration);
        this.wordsHover = EnumConfig.WORDS_HOVER.getStringList(configuration);
        this.wordsExecutables = EnumConfig.WORDS_EXECUTABLES.getExecutableObject(configuration);
        CheckRegister.getInstance(this).update();
    }

    private int serialUID;
    private boolean enableConsoleLogging;
    private String dbType;
    private String username;
    private String password;
    private int port;
    private String address;
    private String database;
    private String table;
    private int autoSave;
    private boolean domainEnabled;
    private String domainRegex;
    private String domainWhitelist;
    private List<String> domainWarning;
    private List<String> domainHover;
    private List<ExecutableObject> domainExecutables;
    private boolean ipv4Enabled;
    private String ipv4Regex;
    private String ipv4Whitelist;
    private List<String> ipv4Warning;
    private List<String> ipv4Hover;
    private List<ExecutableObject> ipv4Executables;
    private boolean wordsEnabled;
    private List<WordsMatcher> blacklistWords;
    private List<String> wordsWarning;
    private List<String> wordsHover;
    private List<ExecutableObject> wordsExecutables;

    public List<String> getListAndReplace(final List<String> list, final String placeholder, final String replace) {
        final List<String> stringList = new ArrayList<>();
        for (final String string : list)
            stringList.add(string.replaceAll(placeholder, replace));
        return stringList;
    }

    public String[] getArrayAndReplace(final List<String> list, final String placeholder, final String replace) {
        final String[] strings = new String[list.size()];
        for (int i = 0; i < strings.length; i++)
            strings[i] = list.get(i).replaceAll(placeholder, replace);
        return strings;
    }

    public int getSerialUID() {
        return serialUID;
    }

    public boolean isEnableConsoleLogging() {
        return enableConsoleLogging;
    }

    public String getDbType() {
        return dbType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public String getDatabase() {
        return database;
    }

    public String getTable() {
        return table;
    }

    public int getAutoSave() {
        return autoSave;
    }

    public boolean isDomainEnabled() {
        return domainEnabled;
    }

    public String getDomainRegex() {
        return domainRegex;
    }

    public String getDomainWhitelist() {
        return domainWhitelist;
    }

    public List<String> getDomainWarning() {
        return domainWarning;
    }

    public List<String> getDomainHover() {
        return domainHover;
    }

    public List<ExecutableObject> getDomainExecutables() {
        return domainExecutables;
    }

    public boolean isIpv4Enabled() {
        return ipv4Enabled;
    }

    public String getIpv4Regex() {
        return ipv4Regex;
    }

    public String getIpv4Whitelist() {
        return ipv4Whitelist;
    }

    public List<String> getIpv4Warning() {
        return ipv4Warning;
    }

    public List<String> getIpv4Hover() {
        return ipv4Hover;
    }

    public List<ExecutableObject> getIpv4Executables() {
        return ipv4Executables;
    }

    public boolean isWordsEnabled() {
        return wordsEnabled;
    }

    public List<WordsMatcher> getBlacklistWords() {
        return blacklistWords;
    }

    public List<String> getWordsWarning() {
        return wordsWarning;
    }

    public List<String> getWordsHover() {
        return wordsHover;
    }

    public List<ExecutableObject> getWordsExecutables() {
        return wordsExecutables;
    }
}
