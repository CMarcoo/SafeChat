package me.thevipershow.safechat.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.postgresql.Driver;
import static me.thevipershow.safechat.sql.SQLPrebuiltStatements.*;

public class PostgreSQLDatabaseManager implements DatabaseManager {
    private static PostgreSQLDatabaseManager instance = null;
    private final HikariDataSource source;
    private final JavaPlugin plugin;
    private final BukkitScheduler scheduler;

    private PostgreSQLDatabaseManager(JavaPlugin plugin, String address, int port, String database, String username, String password) {
        this.source = createDataSource(createConfig(address, port, database, username, password));
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    private PostgreSQLDatabaseManager(JavaPlugin plugin, String address, String database, String username, String password) {
        this.source = createDataSource(createConfig(address, database, username, password));
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    public static PostgreSQLDatabaseManager getInstance(JavaPlugin plugin, String address, int port, String database, String username, String password) {
        return instance != null ? instance : (instance = new PostgreSQLDatabaseManager(plugin, address, port, database, username, password));
    }

    public static PostgreSQLDatabaseManager getInstance(JavaPlugin plugin, String address, String database, String username, String password) {
        return instance != null ? instance : (instance = new PostgreSQLDatabaseManager(plugin, address, database, username, password));
    }

    /**
     * Create an HikariDataSource for PostgreSQL from an HikariConfig
     *
     * @param config a valid config
     * @return a datasource with the PostgreSQL driver
     */
    private HikariDataSource createDataSource(final HikariConfig config) {
        return new HikariDataSource(config);
    }

    /**
     * Creates a new HikariConfig with the given parameters.
     *
     * @param ip       the server address
     * @param database the database name
     * @param username the account's username
     * @param password the account's password
     * @return returns an HikariConfig
     */
    private HikariConfig createConfig(final String ip, final String database, final String username, final String password) {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql://%s:5432/%s", ip, database));
        return getHikariConfig(username, password, config);
    }

    /**
     * Add values to the Hikari Config passed as argument
     *
     * @param username a valid registered username
     * @param password the account password
     * @param config   the HikariConfig that will be modified
     * @return a modified version of the HikariConfig
     */
    private HikariConfig getHikariConfig(final String username, final String password, final HikariConfig config) {
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(Driver.class.getCanonicalName());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "256");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("allowMultiQueries", "true");
        config.setConnectionTimeout(TimeUnit.SECONDS.toMillis(15L));
        return config;
    }

    private HikariConfig createConfig(final String ip, final int port, final String database, final String username, final String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql://%s:%d/%s", ip, port, database));
        return getHikariConfig(username, password, config);
    }

    @Override
    public void createTable(ExceptionHandler handler) {
        SQLUtils.createTable(source::getConnection, POSTGRESQL_CREATE_TABLE, handler);
    }

    @Override
    public void addUniquePlayerOrUpdate(UUID playerUuid, int severity, ExceptionHandler handler) {
        SQLUtils.addUniquePlayerOrUpdate(source::getConnection, POSTGRESQL_ADD_PLAYER_OR_UPDATE, playerUuid, severity, handler);
    }

    @Override
    public CompletableFuture<Integer> getPlayerData(UUID playerUuid) {
        return SQLUtils.getPlayerData(source::getConnection, playerUuid, scheduler, plugin, POSTGRESQL_GET_PLAYER_DATA);
    }

    @Override
    public CompletableFuture<Map<String, Integer>> getTopData(int search) {
        return SQLUtils.getTopData(source::getConnection, scheduler, plugin, POSTGRESQL_GET_TOP_DATA, search);
    }
}
