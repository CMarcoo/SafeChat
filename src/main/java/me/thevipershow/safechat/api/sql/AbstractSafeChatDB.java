package me.thevipershow.safechat.api.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.thevipershow.safechat.SafeChatPlugin;
import me.thevipershow.safechat.api.data.PlayerData;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractSafeChatDB implements ISafeChatDB {

    private final SafeChatPlugin safeChatPlugin;
    private HikariConfig hikariConfig = new HikariConfig();
    private final HikariDataSource hikariDataSource;

    public AbstractSafeChatDB(SafeChatPlugin safeChatPlugin, String urlDbName, String database, String username, String password, String address, int port, int timeout, Class<? extends Driver> driverClass) {
        this.safeChatPlugin = safeChatPlugin;
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setConnectionTimeout(timeout);
        hikariConfig.setDriverClassName(driverClass.getName());
        hikariConfig.setJdbcUrl(String.format("jdbc:%s://%s:%d/%s", urlDbName, address, port, database));
        this.hikariDataSource = new HikariDataSource(this.hikariConfig);
    }

    /**
     * Provide a connection from the database.
     *
     * @return A valid connection.
     */
    @Override
    public @NotNull Optional<Connection> provideConnection() {
        try {
            final Connection connection = this.hikariDataSource.getConnection();
            return Optional.of(connection);
        } catch (SQLException ignored) {
            return Optional.empty();
        }
    }

    /**
     * Every database must have an instance of {@link SafeChatPlugin}.
     *
     * @return The instance of this plugin.
     */
    @Override
    public @NotNull SafeChatPlugin getSafeChatPlugin() {
        return safeChatPlugin;
    }
}
