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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("ExecutableObject")
public final class ExecutableObject implements ConfigurationSerializable {
    private final long serialVersionUID = 1L;
    private final List<String> commands;
    private final int flags;

    public ExecutableObject(List<String> commands, int flags) {
        this.commands = commands;
        this.flags = flags;
    }

    public List<String> getCommands() {
        return commands;
    }

    public int getFlags() {
        return flags;
    }

    @Override
    public final Map<String, Object> serialize() {
        final Map<String, Object> result = new HashMap<>();
        result.put("commands", Objects.requireNonNull(commands));
        result.put("flags", flags);
        return result;
    }

    public static ExecutableObject deserialize(final Map<String, Object> objectMap) {
        final List<String> commands = (List<String>) objectMap.get("commands");
        final int flags = (int) objectMap.get("flags");
        return new ExecutableObject(commands, flags);
    }
}
