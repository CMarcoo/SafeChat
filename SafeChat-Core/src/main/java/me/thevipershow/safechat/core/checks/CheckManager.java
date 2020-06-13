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

package me.thevipershow.safechat.core.checks;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.thevipershow.safechat.core.configuration.AbstractValues;
import me.thevipershow.safechat.core.events.FlagEvent;
import me.thevipershow.safechat.core.sql.data.Flag;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.Bukkit;
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

    public static synchronized CheckManager getInstance(JavaPlugin plugin, AbstractValues values) {
        return instance != null ? instance : (instance = new CheckManager(plugin, values));
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

    public static void doCheck(TextComponent component, Player player, Flag flag) {
        sendIfNotEmptyComponent(component, player);
        Bukkit.getPluginManager().callEvent(new FlagEvent(flag, player.getUniqueId(), player.getName()));
    }

    @EventHandler(ignoreCancelled = true)
    public void check(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (!CheckLogics.domainCheck(event, values))
            doCheck(values.getDomainsComponent(), player, Flag.DOMAINS);
        else if (!CheckLogics.addressCheck(event, values))
            doCheck(values.getIpv4Component(), player, Flag.IPV4);
        else if (!CheckLogics.wordsCheck(event, values))
            doCheck(values.getWordsComponent(), player, Flag.WORDS);
    }
}