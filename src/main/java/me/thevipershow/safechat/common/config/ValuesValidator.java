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

import me.thevipershow.safechat.spigot.config.SpigotValues;
import org.bukkit.plugin.java.JavaPlugin;

public final class ValuesValidator {

    public static ValuesValidator instance = null;
    private final SpigotValues values;
    private final JavaPlugin plugin;

    private ValuesValidator(final SpigotValues values, JavaPlugin plugin) {
        this.values = values;
        this.plugin = plugin;
    }

    public static ValuesValidator getInstance(final SpigotValues values, final JavaPlugin plugin) {
        return instance != null ? instance : (instance = new ValuesValidator(values, plugin));
    }

    public void validateAll() {
        try {
            Validator.validateConfig(values);
        } catch (RuntimeException e) {
            plugin.getLogger().warning("Something is wrong in the config.yml , you should correct it.");
            plugin.getLogger().warning("Issue: " + e.getMessage());
        }
    }
}
