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

import java.util.List;
import me.thevipershow.safechat.common.enums.EnumConfig;
import org.bukkit.configuration.Configuration;

public abstract class AbstractValues {

    protected Configuration configuration;

    protected AbstractValues(final Configuration configuration) {
        this.configuration = configuration;
    }

    protected void updateConfigValues() {
        enableConsoleLogging = EnumConfig.ENABLE_CONSOLE_LOGGING.getBool(configuration);
        serialUID = EnumConfig.SERIAL_UID.getInt(configuration);
        dbType = EnumConfig.DB_TYPE.getString(configuration);
        username = EnumConfig.USERNAME.getString(configuration);
        password = EnumConfig.PASSWORD.getString(configuration);
        port = EnumConfig.PORT.getInt(configuration);
        address = EnumConfig.ADDRESS.getString(configuration);
        database = EnumConfig.DATABASE.getString(configuration);
        table = EnumConfig.TABLE.getString(configuration);
        autoSave = EnumConfig.AUTO_SAVE.getInt(configuration);
        domainEnabled = EnumConfig.DOMAIN_ENABLED.getBool(configuration);
        domainRegex = EnumConfig.DOMAIN_REGEX.getString(configuration);
        domainWhitelist = EnumConfig.DOMAIN_WHITELIST.getString(configuration);
        domainWarning = EnumConfig.DOMAIN_WARNING.getStringList(configuration);
        domainHover = EnumConfig.DOMAIN_HOVER.getStringList(configuration);
        domainExecutables = EnumConfig.DOMAIN_EXECUTABLES.getExecutableObject(configuration);
        ipv4Enabled = EnumConfig.IPV4_ENABLED.getBool(configuration);
        ipv4Regex = EnumConfig.IPV4_REGEX.getString(configuration);
        ipv4Whitelist = EnumConfig.IPV4_WHITELIST.getString(configuration);
        ipv4Warning = EnumConfig.IPV4_WARNING.getStringList(configuration);
        ipv4Hover = EnumConfig.IPV4_HOVER.getStringList(configuration);
        ipv4Executables = EnumConfig.IPV4_EXECUTABLES.getExecutableObject(configuration);
        wordsEnabled = EnumConfig.WORDS_ENABLED.getBool(configuration);
        blacklistWords = EnumConfig.WORDS_BLACKLIST.getWordsMatcherList(configuration);
        wordsWarning = EnumConfig.WORDS_WARNING.getStringList(configuration);
        wordsHover = EnumConfig.WORDS_HOVER.getStringList(configuration);
        wordsExecutables = EnumConfig.WORDS_EXECUTABLES.getExecutableObject(configuration);
    }

    public abstract void updateAll();

    protected int serialUID;
    protected boolean enableConsoleLogging;
    protected String dbType;
    protected String username;
    protected String password;
    protected int port;
    protected String address;
    protected String database;
    protected String table;
    protected int autoSave;
    protected boolean domainEnabled;
    protected String domainRegex;
    protected String domainWhitelist;
    protected List<String> domainWarning;
    protected List<String> domainHover;
    protected List<ExecutableObject> domainExecutables;
    protected boolean ipv4Enabled;
    protected String ipv4Regex;
    protected String ipv4Whitelist;
    protected List<String> ipv4Warning;
    protected List<String> ipv4Hover;
    protected List<ExecutableObject> ipv4Executables;
    protected boolean wordsEnabled;
    protected List<WordsMatcher> blacklistWords;
    protected List<String> wordsWarning;
    protected List<String> wordsHover;
    protected List<ExecutableObject> wordsExecutables;

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
