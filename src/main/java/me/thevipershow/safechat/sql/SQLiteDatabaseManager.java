package me.thevipershow.safechat.sql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import static me.thevipershow.safechat.sql.SQLPrebuiltStatements.*;


public final class SQLiteDatabaseManager implements DatabaseManager {

    private static SQLiteDatabaseManager instance = null;
    private final JavaPlugin plugin;
    private final File dataFolder;
    private final BukkitScheduler scheduler;

    private SQLiteDatabaseManager(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        this.scheduler = plugin.getServer().getScheduler();
        createDatabaseFile(plugin.getDataFolder());
    }

    public final boolean createDatabaseFile(File dataFolder) {
        boolean success = false;
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        final File file = new File(dataFolder, "safechat.sqlite");
        if (!file.exists()) {
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    private Connection getDatabaseConnection() throws SQLException {
        final String url = "jdbc:sqlite:" + this.dataFolder.getAbsolutePath() + File.separator + "safechat.sqlite";
        final Connection con = DriverManager.getConnection(url);
        if (con != null) {
            return con;
        }
        throw new SQLException("Couldn't establish a connection");
    }

    public static SQLiteDatabaseManager getInstance(final JavaPlugin plugin) {
        return instance != null ? instance : (instance = new SQLiteDatabaseManager(plugin));
    }

    @Override
    public final void createTable(ExceptionHandler handler) {
        SQLUtils.createTable(this::getDatabaseConnection, SQLITE_CREATE_TABLE, handler);
    }

    @Override
    public final void addUniquePlayerOrUpdate(UUID playerUuid, int severity, ExceptionHandler handler) {
        SQLUtils.addUniquePlayerOrUpdate(this::getDatabaseConnection, SQLITE_ADD_PLAYER_OR_UPDATE, playerUuid, severity, handler);
    }

    @Override
    public final CompletableFuture<Integer> getPlayerData(UUID playerUuid) {
        return SQLUtils.getPlayerData(this::getDatabaseConnection, playerUuid, scheduler, plugin, SQLITE_GET_PLAYER_DATA);
    }

    @Override
    public final CompletableFuture<Map<String, Integer>> getTopData(int search) {
        return SQLUtils.getTopData(this::getDatabaseConnection, scheduler, plugin, SQLITE_GET_TOP_DATA, search);
    }
}
