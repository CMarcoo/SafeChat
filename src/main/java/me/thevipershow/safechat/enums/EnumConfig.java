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
package me.thevipershow.safechat.enums;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.thevipershow.safechat.config.WordsMatcher;
import org.bukkit.configuration.file.FileConfiguration;

public enum EnumConfig {
    SERIAL_UID("serialUID"),
    ENABLE_CONSOLE_LOGGING("safechat.enable-console-logging"),
    DB_TYPE("safechat.database.type"),
    USERNAME("safechat.database.username"),
    PASSWORD("safechat.database.password"),
    PORT("safechat.database.port"),
    ADDRESS("safechat.database.address"),
    DATABASE("safechat.database.database"),
    TABLE("safechat.database.table"),
    DOMAIN_ENABLED("safechat.domains.enabled"),
    DOMAIN_REGEX("safechat.domains.regex"),
    DOMAIN_WHITELIST("safechat.domains.whitelisted"),
    DOMAIN_WARNING("safechat.domains.warning"),
    DOMAIN_HOVER("safechat.domains.hover-warning"),
    IPV4_ENABLED("safechat.addresses.enabled"),
    IPV4_REGEX("safechat.addresses.regex"),
    IPV4_WHITELIST("safechat.addresses.whitelisted"),
    IPV4_WARNING("safechat.addresses.warning"),
    IPV4_HOVER("safechat.addresses.hover-warning"),
    WORDS_ENABLED("safechat.words.enabled"),
    WORDS_BLACKLIST("safechat.words.blacklisted"),
    WORDS_WARNING("safechat.words.warning"),
    WORDS_HOVER("safechat.words.hover-warning");

    private final String value;

    public final String getString(final FileConfiguration configuration) {
        return configuration.getString(value);
    }

    public final int getInt(final FileConfiguration configuration) {
        return configuration.getInt(value);
    }

    public final boolean getBool(final FileConfiguration configuration) {
        return configuration.getBoolean(value);
    }

    public final List<String> getStringList(final FileConfiguration configuration) {
        return configuration.getStringList(value);
    }

    public final List<WordsMatcher> getWordsMatcherList(final FileConfiguration configuration) {
        return configuration.getMapList(value).stream().map(map -> WordsMatcher.deserialize((Map<String, Object>) map)).collect(Collectors.toList());
    }

    EnumConfig(String value) {
        this.value = value;
    }
}

