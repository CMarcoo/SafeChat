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

package me.thevipershow.safechat.spigot.config;

import me.thevipershow.safechat.common.checks.register.CheckRegister;
import me.thevipershow.safechat.common.config.AbstractValues;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpigotValues extends AbstractValues {
    private final JavaPlugin plugin;

    private SpigotValues(final Configuration configuration, final JavaPlugin plugin) {
        super(configuration);
        this.plugin = plugin;
    }

    private static SpigotValues instance = null;

    public static SpigotValues getInstance(final Configuration configuration, final JavaPlugin plugin) {
        return instance == null ? (instance = new SpigotValues(configuration, plugin)) : instance;
    }

    @Override
    public void updateAll() {
        plugin.reloadConfig();
        super.updateConfigValues();
        CheckRegister.getInstance(this).update();
    }
}
