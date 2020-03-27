package me.thevipershow.safechat;

import com.zaxxer.hikari.HikariDataSource;
import me.thevipershow.safechat.checks.register.CheckRegister;
import me.thevipershow.safechat.commands.SafeChatCommand;
import me.thevipershow.safechat.enums.EnumConfig;
import me.thevipershow.safechat.events.listeners.FlagListener;
import me.thevipershow.safechat.sql.PostgreSQLUtils;
import me.thevipershow.spigotchatlib.SpigotChatLib;
import me.thevipershow.spigotchatlib.exceptions.MissingPluginException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Safechat extends JavaPlugin {

    public HikariDataSource dataSource;

    private static PluginManager pluginManager = Bukkit.getPluginManager();


    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            SpigotChatLib.registerDependency(this);
        } catch (MissingPluginException e) {
            getLogger().severe("SpigotChatLib dependency is missing on the server!");
        }

        saveDefaultConfig();

        if (EnumConfig.ENABLED.getBool()) {
            dataSource = PostgreSQLUtils.createDataSource(
                    PostgreSQLUtils.createConfig(EnumConfig.ADDRESS.getString(),
                            EnumConfig.PORT.getInt(),
                            EnumConfig.DATABASE.getString(),
                            EnumConfig.USERNAME.getString(),
                            EnumConfig.PASSWORD.getString())
            );
            PostgreSQLUtils.createTable(dataSource);
            pluginManager.registerEvents(new FlagListener(dataSource), this);
        }


        pluginManager.registerEvents(new CheckRegister(this), this);
        getCommand("safechat").setExecutor(new SafeChatCommand(this));
    }

}
