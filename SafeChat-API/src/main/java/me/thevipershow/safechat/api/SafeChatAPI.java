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

package me.thevipershow.safechat.api;

import me.thevipershow.safechat.core.sql.databases.DatabaseX;
import me.thevipershow.safechat.plugin.SafeChatPlugin;

public final class SafeChatAPI {
    private static SafeChatAPI instance = null;

    private SafeChatAPI() {
    }

    public static SafeChatAPI getInstance() {
        return instance != null ? instance : (instance = new SafeChatAPI());
    }

    /**
     * Get the an interface that allows to interact with the same database used by the SafeChat plugin.
     *
     * @return The DatabaseX.
     * @throws RuntimeException If either the plugin wasn't still enabled, or the database hasn't been created
     */
    public final DatabaseX getLoadedDatabase() throws RuntimeException {
        SafeChatPlugin plugin = SafeChatPlugin.getInstance();
        if (plugin == null) throw new RuntimeException("SafeChat plugin still hasn't enabled.");
        DatabaseX databaseX = plugin.getDatabaseX();
        if (databaseX == null) throw new RuntimeException("SafeChat's database still hasn't loaded.");
        return databaseX;
    }
}
