package me.thevipershow.safechat.api.sql;

import me.thevipershow.safechat.SafeChatPlugin;
import me.thevipershow.safechat.api.data.PlayerData;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ISafeChatDB {

    /**
     * Provide a connection from the database.
     *
     * @return A valid connection.
     */
    @NotNull
    Optional<Connection> provideConnection();

    /**
     * Every database must have an instance of {@link SafeChatPlugin}.
     *
     * @return The instance of this plugin.
     */
    @NotNull
    SafeChatPlugin getSafeChatPlugin();

    /**
     * Execute any statement using the Database connection.
     * And return a Bukkit-synced Integer-type CompletableFuture.
     *
     * @param SQL The SQL query to execute.
     * @return A Integer-type CompletableFuture that is synchronized with Bukkit's main thread.
     * {@link PreparedStatement#executeUpdate()} too see the meaning of the returned integer.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @NotNull
    default CompletableFuture<Integer> executeStatement(@NotNull String SQL) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        Plugin plugin = getSafeChatPlugin();
        BukkitScheduler scheduler = plugin.getServer().getScheduler();

        Optional<Connection> optionalConnection = provideConnection();
        if (!optionalConnection.isPresent()) {
            future.completeExceptionally(new SQLException("When " + getClass().getName() + " ran query \"" + SQL + "\",\n The database did not respond."));
        }

        scheduler.runTaskAsynchronously(plugin, () -> {
            try (Connection conn = optionalConnection.get();
                 PreparedStatement ps = conn.prepareStatement(SQL)) {
                int updateReturn = ps.executeUpdate();
                scheduler.runTask(plugin, () -> future.complete(updateReturn));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return future;
    }

    /**
     * Get the player data of someone.
     *
     * @param uuid The UUID of the player.
     * @return The completable future with the data (the data may be null).
     */
    @NotNull
    CompletableFuture<PlayerData> getPlayerData(@NotNull UUID uuid);

}
