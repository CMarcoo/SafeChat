package me.thevipershow.safechat;

import me.thevipershow.safechat.checks.register.CheckRegister;
import me.thevipershow.spigotchatlib.SpigotChatLib;
import me.thevipershow.spigotchatlib.exceptions.MissingPluginException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Safechat extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            SpigotChatLib.registerDependency(this);
        } catch (MissingPluginException e) {
            getLogger().severe("SpigotChatLib dependency is missing on the server!");
        }
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new CheckRegister(), this);
    }

}
