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

package me.thevipershow.safechat.bungeecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bukkit.ChatColor;

public class SafeChatBungeecord extends Plugin {

    private final File PLUGIN_DATA_FOLDER = getDataFolder();
    private final File CONFIG = new File(PLUGIN_DATA_FOLDER, "config.yml");
    private final Logger logger = getLogger();

    private void createFileIfNotExists() {
        if (!PLUGIN_DATA_FOLDER.exists())
            PLUGIN_DATA_FOLDER.mkdir();
        if (!CONFIG.exists()) {
            try {
                CONFIG.createNewFile();
            } catch (final IOException e) {
                logger.warning("Could not create the `config.yml`!");
                e.printStackTrace();
            }
        }
    }

    private Configuration loadConfig() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(CONFIG);
        } catch (final IOException e) {
            logger.warning("Could not load `config.yml`!");
            e.printStackTrace();
            return null;
        }
    }

    private void saveConfig(final Configuration configuration) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, CONFIG);
        } catch (final IOException e) {
            logger.warning("Could not save `config.yml`!");
            e.printStackTrace();
        }
    }

    private void saveDefaultConfig() {
        createFileIfNotExists();
        try (final InputStream inputStream = getResourceAsStream("config.yml")) {
            Files.copy(inputStream, CONFIG.toPath());
        } catch (final IOException e) {
            logger.warning("Could not save embedded resource `config.yml`");
            e.printStackTrace();
        }
    }

    @Override
    public final void onLoad() {
        saveDefaultConfig();
        final Configuration config = loadConfig();
        if (config == null) {
            onDisable();
        }

    }

    @Override
    public final void onEnable() {
    }
}
