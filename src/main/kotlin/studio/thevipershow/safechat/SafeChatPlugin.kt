package studio.thevipershow.safechat

import org.bukkit.plugin.java.JavaPlugin
import studio.thevipershow.safechat.config.ConfigManager

/**
 * Main class.
 */
class SafeChatPlugin : JavaPlugin() {

    val configManager = ConfigManager(this)

    /**
     * Called when the plugin is loading.
     */
    override fun onLoad() {
        configManager.saveAllIfAbsent()
    }

    /**
     * Called when the plugin is enabling.
     */
    override fun onEnable() {

    }
}