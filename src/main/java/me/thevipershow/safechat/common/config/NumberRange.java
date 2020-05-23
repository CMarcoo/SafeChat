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

package me.thevipershow.safechat.common.config;

public final class NumberRange<N extends Number & Comparable<N>> extends Range<N> {

    public NumberRange(final N lowerBound,final N upperBound) {
        super(lowerBound, upperBound);
    }

    public static <T extends Number & Comparable<T>> NumberRange<T> process(final T lowerLimit,final T upperLimit) {
        return new NumberRange<>(lowerLimit, upperLimit);
    }

    @Override
    public boolean isInRange(final N subject) {
        return (subject.compareTo(super.lowerBound) >= 0) && (subject.compareTo(super.upperBound) <= 0);
    }
}
