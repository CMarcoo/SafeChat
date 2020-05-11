package me.thevipershow.safechat;

import com.zaxxer.hikari.HikariDataSource;
import java.util.Objects;
import me.thevipershow.safechat.checks.register.CheckRegister;
import me.thevipershow.safechat.commands.SafeChatCommand;
import me.thevipershow.safechat.config.Values;
import me.thevipershow.safechat.events.listeners.FlagListener;
import me.thevipershow.safechat.sql.PostgreSQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Safechat extends JavaPlugin {

    public HikariDataSource dataSource;
    private boolean isOnlineMode;
    private final PluginManager pluginManager = Bukkit.getPluginManager();
    private final Values values = Values.getInstance(this);
    private final CheckRegister checkRegister = CheckRegister.getInstance(this);
    private FlagListener flagListener;

    @Override
    public void onEnable() {
        isOnlineMode = getServer().getOnlineMode();
        saveDefaultConfig();
        if (values.isEnabled()) {
            dataSource = PostgreSQLUtils.createDataSource(
                    PostgreSQLUtils.createConfig(
                            values.getAddress(),
                            values.getPort(),
                            values.getDatabase(),
                            values.getUsername(),
                            values.getPassword())
            );
            flagListener = FlagListener.getInstance(dataSource);
            PostgreSQLUtils.createTable(dataSource);
            pluginManager.registerEvents(flagListener, this);
        }

        pluginManager.registerEvents(checkRegister, this);
        Objects.requireNonNull(getCommand("safechat")).setExecutor(new SafeChatCommand(this, dataSource, isOnlineMode));
    }
}

