/*
 * SafeChat - A Minecraft plugin to keep your chat safe.
 * Copyright (C) 2020 TheViperShow
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

package me.thevipershow.safechat.common.configuration;

import java.util.List;
import lombok.*;
import me.thevipershow.safechat.common.configuration.objects.ExecutableObject;
import me.thevipershow.safechat.common.configuration.objects.WordsMatcher;
import org.bukkit.configuration.Configuration;

@Getter
@RequiredArgsConstructor
public abstract class AbstractValues {
    private final @Getter(AccessLevel.PROTECTED) Configuration configuration;

    /**
     * Update all the config values
     */
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
        domainExecutables = (List<ExecutableObject>) EnumConfig.DOMAIN_EXECUTABLES.get(configuration);
        ipv4Enabled = EnumConfig.IPV4_ENABLED.getBool(configuration);
        ipv4Regex = EnumConfig.IPV4_REGEX.getString(configuration);
        ipv4Whitelist = EnumConfig.IPV4_WHITELIST.getString(configuration);
        ipv4Warning = EnumConfig.IPV4_WARNING.getStringList(configuration);
        ipv4Hover = EnumConfig.IPV4_HOVER.getStringList(configuration);
        ipv4Executables = (List<ExecutableObject>) EnumConfig.IPV4_EXECUTABLES.get(configuration);
        wordsEnabled = EnumConfig.WORDS_ENABLED.getBool(configuration);
        blacklistWords = (List<WordsMatcher>) EnumConfig.WORDS_BLACKLIST.get(configuration);
        wordsWarning = EnumConfig.WORDS_WARNING.getStringList(configuration);
        wordsHover = EnumConfig.WORDS_HOVER.getStringList(configuration);
        wordsExecutables = (List<ExecutableObject>) EnumConfig.WORDS_EXECUTABLES.get(configuration);
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
}
