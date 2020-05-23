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

public enum ANSIColor {
    BLACK("\u001B[30m", "B"),
    RED("\u001B[31m", "r"),
    GREEN("\u001B[32m", "g"),
    YELLOW("\u001B[33m", "y"),
    BLUE("\u001B[34m", "b"),
    MAGENTA("\u001B[35m", "m"),
    CYAN("\u001B[36m", "c"),
    WHITE("\u001B[37m", "w"),
    BLACK_BACKGROUND("\u001B[40m", "Bb"),
    RED_BACKGROUND("\u001B[41m", "rb"),
    GREEN_BACKGROUND("\u001B[42m", "gb"),
    YELLOW_BACKGROUND("\u001B[43m", "yb"),
    BLUE_BACKGROUND("\u001B[44m", "bb"),
    MAGENTA_BACKGROUND("\u001B[45m", "mb"),
    CYAN_BACKGROUND("\u001B[46m", "cb"),
    WHITE_BACKGROUND("\u001B[47m", "wb"),
    RESET("\u001B[0m", "R");

    final String placeholder;
    final String code;

    ANSIColor(String code, String placeholder) {
        this.code = code;
        this.placeholder = placeholder;
    }

    public String code() {
        return code;
    }

    public static String colorString(final char placeholder, String input) {
        for (final ANSIColor value : values()) {
            input = input.replace(placeholder + value.placeholder, value.code);
        }
        return input;
    }
}
