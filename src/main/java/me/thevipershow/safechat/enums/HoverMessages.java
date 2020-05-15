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
package me.thevipershow.safechat.enums;

public enum HoverMessages {

    NO_ARGS("&eSafeChat &7syntax:",
            "&6Open this help page&7:",
            "- &7/safechat",
            "&6Get stored data of a certain player&7:",
            "- &7/safechat sql search &8<&eplayer&8>",
            "&6Get &o&nX&r &6players with the highest flags count&7:",
            "- &7/safechat sql top &8<&enumber&8>",
            "&6Reload all the values from the config.yml",
            "- &7/safechat reload");

    private final String[] messages;

    HoverMessages(String... messages) {
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }
}
