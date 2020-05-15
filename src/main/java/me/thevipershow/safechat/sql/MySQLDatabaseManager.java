package me.thevipershow.safechat.sql;

import com.mysql.jdbc.Driver;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import static me.thevipershow.safechat.sql.SQLPrebuiltStatements.*;

public final class MySQLDatabaseManager implements DatabaseManager {
    private static MySQLDatabaseManager instance = null;
    private final HikariDataSource source;
    private final JavaPlugin plugin;
    private final BukkitScheduler scheduler;

    private MySQLDatabaseManager(JavaPlugin plugin, String address, int port, String database, String username, String password) {
        this.source = HikariDatabaseUtils.createDataSource(HikariDatabaseUtils.createConfig(address, port, database, username, password, Driver.class, HikariDatabaseUtils.DatabaseType.MYSQL));
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    private MySQLDatabaseManager(JavaPlugin plugin, String address, String database, String username, String password) {
        this(plugin, address, 5432, database, username, password);
    }

    public static MySQLDatabaseManager getInstance(JavaPlugin plugin, String address, String database, String username, String password) {
        return instance != null ? instance : (instance = new MySQLDatabaseManager(plugin, address, database, username, password));
    }

    public static MySQLDatabaseManager getInstance(JavaPlugin plugin, String address, int port, String database, String username, String password) {
        return instance != null ? instance : (instance = new MySQLDatabaseManager(plugin, address, port, database, username, password));
    }

    @Override
    public final void createTable(ExceptionHandler handler) {
        SQLUtils.createTable(source::getConnection, MYSQL_CREATE_TABLE, handler);
    }

    @Override
    public final void addUniquePlayerOrUpdate(UUID playerUuid, int severity, ExceptionHandler handler) {
        SQLUtils.addUniquePlayerOrUpdate(source::getConnection, MYSQL_ADD_PLAYER_OR_UPDATE, playerUuid, severity, handler);
    }

    @Override
    public final CompletableFuture<Integer> getPlayerData(UUID playerUuid) {
        return SQLUtils.getPlayerData(source::getConnection, playerUuid, scheduler, plugin, POSTGRESQL_GET_PLAYER_DATA);
    }

    @Override
    public final CompletableFuture<Map<String, Integer>> getTopData(int search) {
        return SQLUtils.getTopData(source::getConnection, scheduler, plugin, POSTGRESQL_GET_TOP_DATA, search);
    }
}
