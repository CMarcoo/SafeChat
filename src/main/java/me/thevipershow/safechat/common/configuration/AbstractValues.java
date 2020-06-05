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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.thevipershow.safechat.common.configuration.objects.ExecutableObject;
import me.thevipershow.safechat.common.configuration.objects.WordsMatcher;
import me.thevipershow.safechat.common.configuration.validator.ValuesValidator;
import net.kyori.text.TextComponent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.Configuration;

@Getter
@RequiredArgsConstructor
public abstract class AbstractValues {
    private final @Getter(AccessLevel.PROTECTED)
    Configuration configuration;

    /**
     * Update all the config values
     */
    protected void updateConfigValues() throws IllegalArgumentException {
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
        domainExecutables = EnumConfig.DOMAIN_EXECUTABLES.getExecutableObject(configuration);
        ipv4Enabled = EnumConfig.IPV4_ENABLED.getBool(configuration);
        ipv4Regex = EnumConfig.IPV4_REGEX.getString(configuration);
        ipv4Whitelist = EnumConfig.IPV4_WHITELIST.getString(configuration);
        ipv4Executables = EnumConfig.IPV4_EXECUTABLES.getExecutableObject(configuration);
        wordsEnabled = EnumConfig.WORDS_ENABLED.getBool(configuration);
        blacklistWords = EnumConfig.WORDS_BLACKLIST.getWordsMatcherList(configuration);
        wordsExecutables = EnumConfig.WORDS_EXECUTABLES.getExecutableObject(configuration);

        domainsComponent = buildKashikeComponent(EnumConfig.DOMAIN_WARNING.getStringList(configuration), EnumConfig.DOMAIN_HOVER.getStringList(configuration));
        ipv4Component = domainsComponent = buildKashikeComponent(EnumConfig.IPV4_WARNING.getStringList(configuration), EnumConfig.IPV4_HOVER.getStringList(configuration));
        wordsComponent = domainsComponent = buildKashikeComponent(EnumConfig.WORDS_WARNING.getStringList(configuration), EnumConfig.WORDS_HOVER.getStringList(configuration));

        verifyAll();
    }

    private TextComponent buildKashikeComponent(List<String> strings, List<String> hoverString) {
        if (strings.isEmpty())
            return TextComponent.empty();
        TextComponent.Builder builder = TextComponent.builder();
        TextComponent message = LegacyComponentSerializer.INSTANCE.deserialize(String.join("\n", strings), '&');
        if (!hoverString.isEmpty())
            message.hoverEvent(HoverEvent.showText(LegacyComponentSerializer.INSTANCE.deserialize(String.join("\n", hoverString), '&')));
        builder.append(message);
        return builder.build();
    }

    public void verifyAll() throws IllegalArgumentException {
        ValuesValidator.validateDatabasePort(this);
        ValuesValidator.validateDatabaseType(this);
    }

    public abstract void updateAll() throws IllegalArgumentException;

    TextComponent domainsComponent;
    TextComponent ipv4Component;
    TextComponent wordsComponent;

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
    protected List<ExecutableObject> domainExecutables;
    protected boolean ipv4Enabled;
    protected String ipv4Regex;
    protected String ipv4Whitelist;
    protected List<ExecutableObject> ipv4Executables;
    protected boolean wordsEnabled;
    protected List<WordsMatcher> blacklistWords;
    protected List<ExecutableObject> wordsExecutables;
}
