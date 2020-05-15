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

package me.thevipershow.safechat.events.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.safechat.sql.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class FlagListener implements Listener {
    private static FlagListener instance = null;
    private final DatabaseManager databaseManager;
    private final Logger logger;
    private final Values values;

    private FlagListener(DatabaseManager databaseManager, Logger logger, Values values) {
        this.databaseManager = databaseManager;
        this.logger = logger;
        this.values = values;
    }

    public static FlagListener getInstance(DatabaseManager databaseManager, Logger logger, Values values) {
        return instance != null ? instance : (instance = new FlagListener(databaseManager, logger, values));
    }

    @EventHandler(ignoreCancelled = true)
    public void onFlagThrown(FlagThrownEvent event) {
        if (values.isEnableConsoleLogging())
            logger.info("Player: " + event.getPlayerName() + " has failed a " + event.getCheckName().toUpperCase() + " check!");

        databaseManager.addUniquePlayerOrUpdate(event.getSenderUUID(), event.getSeverity(),
                e -> logger.log(Level.WARNING, "Something went wrong while trying to update values for {0}!\n", event.getPlayerName()));
    }
}
