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

package me.thevipershow.safechat.common.checks;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.thevipershow.safechat.common.configuration.AbstractValues;
import me.thevipershow.safechat.common.sql.DataManager;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CheckManager implements Listener {
    private static CheckManager instance = null;
    private final JavaPlugin plugin;
    private final AbstractValues values;
    private final DataManager dataManager;

    public static synchronized CheckManager getInstance(JavaPlugin plugin, AbstractValues values, DataManager manager) {
        if (instance == null) {
            CheckManager checkManager = new CheckManager(plugin, values, manager);
            plugin.getServer().getPluginManager().registerEvents(checkManager, plugin);
            instance = checkManager;
        }
        return instance;
    }

    /**
     * This method should send a message to an online player if the component isn't empty.
     *
     * @param component The {@link TextComponent} that should be sent.
     * @param player    The Player who will recieve the message.
     */
    public static void sendIfNotEmptyComponent(TextComponent component, Player player) {
        if (!component.isEmpty())
            TextAdapter.sendMessage(player, component);
    }

    @EventHandler(ignoreCancelled = true)
    public void check(AsyncPlayerChatEvent event) {
        if (!CheckLogics.domainCheck(event, values)) { //when a check fails, we'll proceed to the next one.
            sendIfNotEmptyComponent(values.getDomainsComponent(), event.getPlayer());
        } else if (!CheckLogics.addressCheck(event, values)) {
            sendIfNotEmptyComponent(values.getIpv4Component(), event.getPlayer());
        } else if (!CheckLogics.wordsCheck(event, values)) {
            sendIfNotEmptyComponent(values.getWordsComponent(), event.getPlayer());
        }
    }
}