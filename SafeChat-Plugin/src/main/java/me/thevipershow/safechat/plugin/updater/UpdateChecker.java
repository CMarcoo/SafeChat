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

package me.thevipershow.safechat.plugin.updater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.UUID;
import org.bukkit.event.EventPriority;

public final class UpdateChecker {

    private static UpdateChecker instance = null;
    private static final String SPIGOT_RESOURCE_URL = "https://api.spigotmc.org/simple/0.1/index.php?action=getResource&id=79115";
    private static final String SPLIT_REGEX = "\\.";
    private final String currentVersion;
    private final Plugin plugin;

    public static UpdateChecker getInstance(Plugin plugin) {
        return instance == null ? (instance = new UpdateChecker(plugin)) : instance;
    }

    private UpdateChecker(Plugin plugin) {
        this.currentVersion = plugin.getDescription().getVersion();
        this.plugin = plugin;
    }

    private String getLatestVersion() throws IOException {
        URL url = new URL(SPIGOT_RESOURCE_URL);
        try (InputStream inputStream = url.openStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            StringBuilder stringBuilder = new StringBuilder();
            int c;
            while ((c = bufferedReader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            JsonObject jsonObject = new JsonParser().parse(stringBuilder.toString()).getAsJsonObject();
            return jsonObject.get("current_version").getAsString();
        }
    }

    /**
     * Checks the latest build from Spigot and compares it to the current one.
     *
     * @return if this plugin has a newer version available for download.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    private boolean hasNewerVersion() {
        int[] currentBuildInt = new int[3];
        int[] latestBuildInt = new int[3];
        String[] currentBuildStr = currentVersion.split(SPLIT_REGEX);
        String[] latestBuildStr;
        try {
            for (int i = 0; i < 3; i++) {
                currentBuildInt[i] = Integer.parseInt(currentBuildStr[i]);
            }
            String latestVersion = getLatestVersion();
            latestBuildStr = latestVersion.split(SPLIT_REGEX);
            for (int i = 0; i < 3; i++) {
                latestBuildInt[i] = Integer.parseInt(latestBuildStr[i]);
            }
            for (int i = 0; i < 3; i++) {
                if (latestBuildInt[i] > currentBuildInt[i]) {
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            plugin.getLogger().warning(String.format("The plugin version has an invalid format (%s).", currentVersion));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void registerUpdatersIfOutdated() {
        if (hasNewerVersion()) {
            plugin.getLogger().info("The plugin has found a newer version available on Spigot, please update it!");
            class OutdatedNotifier implements Listener {

                private final HashSet<UUID> alreadyNotified = new HashSet<>();

                @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
                public void onPlayerJoin(PlayerJoinEvent event) {
                    UUID uuid = event.getPlayer().getUniqueId();
                    if (!event.getPlayer().isOp() || !event.getPlayer().hasPermission("safechat.updates")) {
                        return;
                    }
                    if (alreadyNotified.contains(uuid)) {
                        return;
                    }
                    event.getPlayer().sendMessage("§8[§6SafeChat§8]§7: §aThere are new updates for SafeChat, please install them!");
                    alreadyNotified.add(uuid);
                }
            }
            Bukkit.getPluginManager().registerEvents(new OutdatedNotifier(), plugin);
        }
    }
}
