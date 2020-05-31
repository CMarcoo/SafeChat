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

package me.thevipershow.safechat.spigot.checks;

import java.util.HashSet;
import java.util.Set;
import me.thevipershow.safechat.common.config.AbstractValues;
import me.thevipershow.safechat.common.enums.SPermissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class CheckRegister implements Listener {

    private static CheckRegister instance = null;

    private final Set<ChatCheck> chatChecks = new HashSet<>();
    private final AbstractValues values;

    private CheckRegister(final AbstractValues values) {
        this.values = values;
        if (instance == null) {
            update();
        }
    }

    public final void update() {
        chatChecks.clear();
        if (values.isWordsEnabled()) {
            chatChecks.add(WordsCheck.getInstance(values));
        }
        if (values.isIpv4Enabled()) {
            chatChecks.add(AddressesCheck.getInstance(values));
        }
        if (values.isDomainEnabled()) {
            chatChecks.add(DomainsCheck.getInstance(values));
        }
    }

    public final boolean addCheck(final ChatCheck chatCheck) {
        return chatChecks.add(chatCheck);
    }

    public final boolean removeCheck(final ChatCheck chatCheck) {
        return chatChecks.remove(chatCheck);
    }

    public static CheckRegister getInstance(final AbstractValues values) {
        return instance != null ? instance : (instance = new CheckRegister(values));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission(SPermissions.BYPASS.getPermission())) {
            for (final ChatCheck check : this.chatChecks) {
                check.result(event.getMessage(), event);
            }
        }
    }
}