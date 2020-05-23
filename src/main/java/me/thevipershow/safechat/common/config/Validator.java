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

import me.thevipershow.safechat.common.sql.ExceptionHandler;
import org.yaml.snakeyaml.error.YAMLException;

public final class Validator {

    @SafeVarargs
    public static <T> boolean validate(final T subject,final ExceptionHandler handler,final T... validOptions) {
        for (T t : validOptions) {
            if (t.equals(subject)) {
                return true;
            }
        }
        final YAMLException e = new YAMLException("The value was invalid »" + subject.toString());
        handler.handle(e);
        throw e;
    }

    public static <N extends Number & Comparable<N>> boolean validateInRange(final N number,final ExceptionHandler handler,final NumberRange<N> range) {
        if (range.isInRange(number)) {
            return true;
        }
        final YAMLException e = new YAMLException("The number is outside valid range [" + range.lowerBound + "-" + range.upperBound + "]");
        handler.handle(e);
        throw e;
    }

    public static boolean validateNotNull(final Object o,final ExceptionHandler handler) {
        if (o != null) {
            return true;
        }
        final YAMLException e = new YAMLException("List inside the config.yml can't be null!");
        handler.handle(e);
        throw e;
    }
}
