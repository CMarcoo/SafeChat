package me.thevipershow.safechat.sql;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import me.thevipershow.safechat.Safechat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.postgresql.Driver;

public class PostgreSQLUtils {


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
    public static HikariConfig createConfig(final String ip, final String database, final String username, final String password) {
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
    private static HikariConfig getHikariConfig(final String username, final String password, final HikariConfig config) {
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

    public static HikariConfig createConfig(final String ip, final int port, final String database, final String username, final String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:postgresql://%s:%d/%s", ip, port, database));
        return getHikariConfig(username, password, config);
    }

    /**
     * Create a table if it doesn't already exist
     * Async [✓]
     *
     * @param source the source
     */
    public static void createTable(HikariDataSource source) {
        assert source != null : "The source was found to be null!";
        Bukkit.getScheduler().runTaskAsynchronously(Safechat.getPlugin(Safechat.class), () -> {
            try {
                try (final Connection connection = source.getConnection(); final PreparedStatement preparedStatement = connection.prepareStatement(
                        "create table if not exists safechat_data\n" +
                                "(\n" +
                                "\tplayer_uuid uuid not null unique primary key ,\n" +
                                "\tflags int not null);")) {
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        });
    }

    public static void addUniquePlayer(final HikariDataSource source, final UUID uuid, final int severity) {
        Bukkit.getScheduler().runTaskAsynchronously(Safechat.getPlugin(Safechat.class), () -> {
            try {
                try (final Connection connection = source.getConnection(); final PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into safechat_data (player_uuid, flags) values ('" + uuid.toString() + "',1) on conflict (player_uuid) do update set flags = safechat_data.flags +" + severity + ";"
                )) {
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<LinkedHashMap<UUID, Integer>> getTopData(final HikariDataSource source, final int limit) {
        final CompletableFuture<LinkedHashMap<UUID, Integer>> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            try {
                try (final Connection connection = source.getConnection(); final PreparedStatement ps = connection.prepareStatement(
                        "select player_uuid, flags from safechat_data order by flags desc limit " + limit + ";"
                )) {
                    final LinkedHashMap<UUID, Integer> playersFlagsMap = new LinkedHashMap<>();
                    final ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        final UUID uuidOfPlayer = UUID.fromString(resultSet.getString("player_uuid"));
                        final int flagsOfPlayer = resultSet.getInt("flags");
                        playersFlagsMap.put(uuidOfPlayer, flagsOfPlayer);
                    }
                    connection.close();
                    completableFuture.complete(playersFlagsMap);
                }
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        });

        return completableFuture;
    }

    public static CompletableFuture<Integer> getPlayerScore(final HikariDataSource source, final String name) {
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(name));
        final CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            if (offlinePlayer.getName() != null) {

                try {
                    try (final Connection con = source.getConnection(); final PreparedStatement ps = con.prepareStatement(
                            "select flags from safechat_data where player_uuid = " + offlinePlayer.getUniqueId().toString() + ";"
                    )) {
                        final ResultSet resultSet = ps.executeQuery();
                        int playerFlags = (resultSet.getInt("flags"));
                        completableFuture.complete(playerFlags);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                completableFuture.complete(-1);
            }
        });
        return completableFuture;
    }


}
