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

package me.thevipershow.safechat.plugin;

import me.thevipershow.safechat.common.checks.CheckManager;
import me.thevipershow.safechat.common.configuration.AbstractValues;
import me.thevipershow.safechat.common.configuration.ValuesImplementation;
import me.thevipershow.safechat.common.configuration.objects.ExecutableObject;
import me.thevipershow.safechat.common.configuration.objects.WordsMatcher;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class SafeChatPlugin extends JavaPlugin {

    /**
     * This methods registers all of the YAML objects that will be read from the config.yml
     */
    private void registerConfigurationSerializer() {
        ConfigurationSerialization.registerClass(ExecutableObject.class);
        ConfigurationSerialization.registerClass(WordsMatcher.class);
    }

    /**
     * This methods creates a new instance of the {@link AbstractValues} class
     * It also checks for eventual issues in the configuration and prints an error
     * if something has gone wrong
     *
     * @return a new AbstractValues object.
     */
    @NotNull
    private AbstractValues getAndUpdate() {
        AbstractValues values = ValuesImplementation.getInstance(getConfig(), this);
        try {
            values.updateAll();
        } catch (IllegalArgumentException e) {
            getLogger().warning("Something has went wrong while updating the config.yml");
            e.printStackTrace();
        }
        return values;
    }

    @Override
    public final void onEnable() { // startup logic:
        registerConfigurationSerializer();
        saveDefaultConfig();
        AbstractValues values = getAndUpdate();
        CheckManager checkManager = CheckManager.getInstance(this, values);
    }
}
