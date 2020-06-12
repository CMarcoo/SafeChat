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

package me.thevipershow.safechat.common.configuration.validator;

import java.util.Locale;
import lombok.experimental.UtilityClass;
import me.thevipershow.safechat.common.configuration.AbstractValues;

@UtilityClass
public class ValuesValidator {

    /**
     * This method validates a port.
     *
     * @param values An implementation {@link AbstractValues}
     * @throws IllegalArgumentException If the port was either less than 1 or greater than 65535.
     */
    public void validateDatabasePort(AbstractValues values) throws IllegalArgumentException {
        if (values.getPort() > 65535 || values.getPort() < 1)
            throw new IllegalArgumentException("A database port must be in range 1-65535.");
    }

    /**
     * This methods validates a database type string.
     *
     * @param values An implementation of {@link AbstractValues}
     * @throws IllegalArgumentException If the database type was unknown.
     */
    public void validateDatabaseType(AbstractValues values) throws IllegalArgumentException {
        switch (values.getDbType().toLowerCase(Locale.ROOT)) {
            case "sqlite":
            case "mysql":
            case "postgresql":
            case "mariadb":
                break;
            default:
                throw new IllegalArgumentException("Invalid database type.");
        }
    }
}
