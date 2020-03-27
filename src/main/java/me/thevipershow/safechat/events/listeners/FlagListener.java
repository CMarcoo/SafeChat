package me.thevipershow.safechat.events.listeners;

import com.zaxxer.hikari.HikariDataSource;
import me.thevipershow.safechat.events.FlagThrownEvent;
import me.thevipershow.safechat.sql.PostgreSQLUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FlagListener implements Listener {

    private final HikariDataSource dataSource;

    public FlagListener(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Listener for the PostgreSQL database values
     * @param event whenever a player's message is flagged.
     */
    @EventHandler
    public void event(FlagThrownEvent event) {
        PostgreSQLUtils.addUniquePlayer(dataSource, event.getSenderUUID(), event.getSeverity());
    }

}
