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

package me.thevipershow.safechat.common.gui;

import java.util.Arrays;
import me.thevipershow.spigotchatlib.chat.TextMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@SuppressWarnings("ConstantConditions")
public final class GUIUtils {
    public static Inventory createInventory(final Player player, final byte size, final String title) {
        return Bukkit.createInventory(player, size, TextMessage.build(title).color().getText());
    }

    public static ItemStack createHead(final OfflinePlayer offlinePlayer) {
        final ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        final SkullMeta skull = (SkullMeta) itemStack.getItemMeta();
        skull.setOwningPlayer(offlinePlayer);
        itemStack.setItemMeta(skull);
        return itemStack;
    }

    public static ItemStack applyMeta(final ItemStack itemStack, final String name, final String... lore) {
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(TextMessage.build(name).color().getText());
        itemMeta.setLore(Arrays.asList(TextMessage.build(lore).color().getText().split("\\r?\\n")));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_POTION_EFFECTS);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
