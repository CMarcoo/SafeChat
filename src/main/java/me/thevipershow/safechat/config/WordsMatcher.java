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

package me.thevipershow.safechat.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("WordsMatcher")
public final class WordsMatcher implements ConfigurationSerializable, Cloneable {
    private final long serialVersionUID = 1L;
    private final String pattern;
    private final String replace;

    public WordsMatcher(String pattern, String replace) {
        this.pattern = pattern;
        this.replace = replace;
    }

    public final String getPattern() {
        return pattern;
    }

    public final String getReplace() {
        return replace;
    }

    public final Pattern getCompiledPattern() {
        return Pattern.compile(this.pattern);
    }

    // Can throw NullPointerException
    @Override
    public final Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("pattern", Objects.requireNonNull(pattern));
        result.put("replace", Objects.requireNonNull(replace));
        return result;
    }

    public static WordsMatcher deserialize(Map<String, Object> objectMap) {
        final String pattern = (String) objectMap.get("pattern");
        final String replace = (String) objectMap.get("replace");
        return new WordsMatcher(pattern, replace);
    }
}
