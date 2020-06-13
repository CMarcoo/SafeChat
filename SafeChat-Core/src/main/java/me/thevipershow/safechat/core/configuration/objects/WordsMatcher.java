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

package me.thevipershow.safechat.core.configuration.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("WordsMatcher")
@RequiredArgsConstructor
@Getter
public final class WordsMatcher implements ConfigurationSerializable, Cloneable {
    private final Pattern pattern;
    private final String replace;

    @Override
    public final Map<String, Object> serialize() {
        final Map<String, Object> result = new HashMap<>();
        result.put("pattern", Objects.requireNonNull(pattern));
        result.put("replace", Objects.requireNonNull(replace));
        return result;
    }

    public static WordsMatcher deserialize(Map<String, Object> map) {
        return new WordsMatcher(
                Pattern.compile((String) map.get("pattern")),
                (String) map.get("replace")
        );
    }
}
