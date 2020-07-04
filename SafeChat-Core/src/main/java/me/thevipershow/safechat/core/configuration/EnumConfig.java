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

package me.thevipershow.safechat.core.configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.thevipershow.safechat.core.configuration.objects.ExecutableObject;
import me.thevipershow.safechat.core.configuration.objects.WordsMatcher;
import org.bukkit.configuration.Configuration;

public enum EnumConfig {
    SERIAL_UID("serialUID"),
    ENABLE_CONSOLE_LOGGING("safechat.enable-console-logging"),
    UPDATES_WARNING("safechat.updates-warning"),
    DB_TYPE("safechat.database.type"),
    USERNAME("safechat.database.username"),
    PASSWORD("safechat.database.password"),
    PORT("safechat.database.port"),
    ADDRESS("safechat.database.address"),
    DATABASE("safechat.database.database"),
    TABLE("safechat.database.table"),
    AUTO_SAVE("safechat.database.auto-save"),
    DOMAIN_ENABLED("safechat.domains.enabled"),
    DOMAIN_REGEX("safechat.domains.regex"),
    DOMAIN_WHITELIST("safechat.domains.whitelisted"),
    DOMAIN_WARNING("safechat.domains.warning"),
    DOMAIN_HOVER("safechat.domains.hover-warning"),
    DOMAIN_EXECUTABLES("safechat.domains.executables"),
    IPV4_ENABLED("safechat.addresses.enabled"),
    IPV4_REGEX("safechat.addresses.regex"),
    IPV4_WHITELIST("safechat.addresses.whitelisted"),
    IPV4_WARNING("safechat.addresses.warning"),
    IPV4_HOVER("safechat.addresses.hover-warning"),
    IPV4_EXECUTABLES("safechat.addresses.executables"),
    WORDS_ENABLED("safechat.words.enabled"),
    WORDS_BLACKLIST("safechat.words.blacklisted"),
    WORDS_WARNING("safechat.words.warning"),
    WORDS_HOVER("safechat.words.hover-warning"),
    WORDS_EXECUTABLES("safechat.words.executables");

    private final String value;

    public String getString(Configuration configuration) {
        return configuration.getString(value);
    }

    public int getInt(Configuration configuration) {
        return configuration.getInt(value);
    }

    public boolean getBool(Configuration configuration) {
        return configuration.getBoolean(value);
    }

    public List<String> getStringList(Configuration configuration) {
        return configuration.getStringList(value);
    }

    public Object get(Configuration configuration) {
        return configuration.get(value);
    }

    /**
     * This method is used to deserialize a list of {@link WordsMatcher} from a yaml configuration.
     *
     * @param configuration The YAML configuration that will provide the values.
     * @return A list of WordsMatcher.
     */
    public final List<WordsMatcher> getWordsMatcherList(final Configuration configuration) {
        return configuration.getMapList(value).parallelStream()
                .map(map -> WordsMatcher.deserialize((Map<String, Object>) map))
                .collect(Collectors.toList());
    }

    /**
     * This method is used to deserialize a list of {@link ExecutableObject} from a yaml configuration.
     *
     * @param configuration The YAML configuration that will provide the values.
     * @return A list of ExecutableObject.
     */
    public final List<ExecutableObject> getExecutableObject(final Configuration configuration) {
        return configuration.getMapList(value).parallelStream()
                .map(map -> ExecutableObject.deserialize((Map<String, Object>) map))
                .collect(Collectors.toList());
    }

    EnumConfig(final String value) {
        this.value = value;
    }
}
