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
package me.thevipershow.safechat.common.enums;

public enum HoverMessages {

    NO_ARGS("&8[&6SafeChat&8]&7: syntax:",
            "&8Open this help page&7:",
            "&f- &7/safechat",
            "&8Get all stored data of a certain player&7:",
            "&f- &7/safechat search &8[&6ipv4&7|&6domains&7|&6words&8] &8<&6player&8>",
            "&8Get flag count of a certain player&7:",
            "&f- &7/safechat search &8<&6player&8>",
            "&8Get &o&nX&r &8players with the highest flags count&7:",
            "&f- &7/safechat top &8<&6number&8> &8[&6ipv4&7|&6domains&7|&6words&8]",
            "&8Clear all data of a certain player",
            "&f- &7/safechat clear &8<&6player&8>",
            "&8Clear specific flags of a certain player",
            "&f- &7/safechat clear &8<&6player&8> &8[&6ipv4&7|&6domains&7|&6words&8]",
            "&8Reload all the values from the config.yml",
            "&f- &7/safechat reload");

    private final String[] messages;

    HoverMessages(final String... messages) {
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }
}
