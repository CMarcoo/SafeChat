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

public enum TicksConverter {
    SECONDS(20),
    MINUTES(20 * 60),
    HOURS(20 * 60 * 60),
    DAYS(20 * 60 * 60 * 24),
    WEEKS(20 * 60 * 60 * 24 * 7),
    YEARS(20 * 60 * 60 * 365);

    private final int conversion;

    TicksConverter(final int conversion) {
        this.conversion = conversion;
    }

    public long convert(final long value) {
        return value * conversion;
    }
}
