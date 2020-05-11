package me.thevipershow.safechat.events.listeners;

import com.zaxxer.hikari.HikariDataSource;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.safechat.sql.PostgreSQLUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FlagListener implements Listener {

    private static FlagListener instance = null;

    private final HikariDataSource dataSource;

    private FlagListener(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static FlagListener getInstance(HikariDataSource dataSource) {
        if (instance == null) {
            instance = new FlagListener(dataSource);
        }
        return instance;
    }

    /**
     * Listener for the PostgreSQL database values
     *
     * @param event whenever a player's message is flagged.
     */
    @EventHandler
    public void event(FlagThrownEvent event) {
        PostgreSQLUtils.addUniquePlayer(dataSource, event.getSenderUUID(), event.getSeverity());
    }
}

