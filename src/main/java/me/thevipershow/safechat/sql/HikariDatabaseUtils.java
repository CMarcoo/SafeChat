package me.thevipershow.safechat.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Driver;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HikariDatabaseUtils {
    public enum DatabaseType {
        POSTGRESQL,
        MYSQL,
    }

    /**
     * Create an HikariDataSource for PostgreSQL from an HikariConfig
     *
     * @param config a valid config
     * @return a datasource with the PostgreSQL driver
     */
    public static HikariDataSource createDataSource(final HikariConfig config) {
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
    public static HikariConfig createConfig(
            final String ip,
            final String database,
            final String username,
            final String password,
            final DatabaseType databaseType,
            final Class<? extends Driver> driverClass) {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:" + databaseType.name().toLowerCase(Locale.getDefault()) + "://%s:5432/%s", ip, database));
        return getHikariConfig(username, password, config, driverClass);
    }

    /**
     * Add values to the Hikari Config passed as argument
     *
     * @param username a valid registered username
     * @param password the account password
     * @param config   the HikariConfig that will be modified
     * @return a modified version of the HikariConfig
     */
    public static HikariConfig getHikariConfig(final String username, final String password, final HikariConfig config, final Class<? extends Driver> driverClass) {
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClass.getCanonicalName());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "256");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("allowMultiQueries", "true");
        config.setConnectionTimeout(TimeUnit.SECONDS.toMillis(15L));
        return config;
    }

    public static HikariConfig createConfig(final String ip, final int port, final String database, final String username, final String password, final Class<? extends Driver> driverClass, final DatabaseType databaseType) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:" + databaseType.name().toLowerCase() + "://%s:%d/%s", ip, port, database));
        return getHikariConfig(username, password, config, driverClass);
    }
}
