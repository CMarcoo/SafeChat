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

package me.thevipershow.safechat.gui;

import java.util.HashSet;
import org.bukkit.inventory.Inventory;

public final class InventoryManager {
    private static InventoryManager instance = null;
    private final HashSet<Inventory> activePlayersGui;

    private InventoryManager() {
        this.activePlayersGui = new HashSet<>();
    }

    public static InventoryManager getInstance() {
        return instance != null ? instance : (instance = new InventoryManager());
    }

    public final boolean removePlayer(final Inventory inventory) {
        return this.activePlayersGui.remove(inventory);
    }

    public final boolean addPlayer(final Inventory inventory) {
        return this.activePlayersGui.add(inventory);
    }

    public final void clear() {
        this.activePlayersGui.clear();
    }

    public final boolean isFound(final Inventory inventory) {
        return this.activePlayersGui.stream().anyMatch(i -> i == inventory);
    }
}