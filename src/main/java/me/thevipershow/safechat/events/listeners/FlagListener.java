package me.thevipershow.safechat.events.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.safechat.sql.DatabaseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FlagListener implements Listener {
    private static FlagListener instance = null;
    private final DatabaseManager databaseManager;
    private final Logger logger;

    private FlagListener(DatabaseManager databaseManager, Logger logger) {
        this.databaseManager = databaseManager;
        this.logger = logger;
    }

    public static FlagListener getInstance(DatabaseManager databaseManager, Logger logger) {
        return instance != null ? instance : (instance = new FlagListener(databaseManager, logger));
    }

    @EventHandler(ignoreCancelled = true)
    public void onFlagThrown(FlagThrownEvent event) {
        databaseManager.addUniquePlayerOrUpdate(event.getSenderUUID(), event.getSeverity(),
                e -> logger.log(Level.WARNING, "Something went wrong while trying to update values for {0}!\n", event.getPlayerName()));
    }
}
