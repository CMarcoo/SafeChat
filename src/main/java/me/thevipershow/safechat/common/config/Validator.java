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

import me.thevipershow.safechat.spigot.config.SpigotValues;

public final class Validator {

    public static boolean equalsToAny(String s ,String... strings) {
        for (String string : strings)
            if (s.equalsIgnoreCase(string))
                return true;

        return false;
    }

    public static void validateConfig(SpigotValues values) throws RuntimeException {
        if (values.address == null)
            throw new RuntimeException("Address in config.yml is invalid!");
        if (values.database == null)
            throw new RuntimeException("Database in config.yml is invalid!");
        if (!equalsToAny(values.dbType, "sqlite","mariadb","postgresql","mysql"))
            throw new RuntimeException("Database type in config.yml is invalid!");
        if (values.domainRegex == null)
            throw new RuntimeException("Domains regex in config.yml is invalid!");
        if (values.domainWhitelist == null)
            throw new RuntimeException("Domains whitelist in config.yml is invalid");
        if (values.username == null)
            throw new RuntimeException("Username in config.yml is invalid");
        if (values.password == null)
            throw new RuntimeException("Password in config.yml is invalid");
        if (values.ipv4Regex == null)
            throw new RuntimeException("IPv4 RegEx in config.yml is invalid");
        if (values.ipv4Whitelist == null)
            throw new RuntimeException("IPv4 Whitelist in config.yml is invalid");

        if (values.domainWarning.isEmpty() && !values.domainHover.isEmpty())
            throw new RuntimeException("Domain hover can't be enabled while message is disabled!");
        if (values.ipv4Whitelist.isEmpty() && !values.ipv4Hover.isEmpty())
            throw new RuntimeException("IPv4 hover can't be enabled while message is disabled!");
        if (values.wordsWarning.isEmpty() && !values.wordsHover.isEmpty())
            throw new RuntimeException("Words hover can't be enabled while message is disabled");

        if (values.port < 1 || values.port > 65535)
            throw new RuntimeException("Port value must be in range 1-65535");
    }
}
