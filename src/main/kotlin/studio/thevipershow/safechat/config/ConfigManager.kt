package studio.thevipershow.safechat.config

import org.bukkit.plugin.java.JavaPlugin
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

class ConfigManager(private val javaPlugin: JavaPlugin) {

    val loadedConfigs = EnumMap<ConfigType, TomlFile>(ConfigType::class.java)

    private fun saveIfAbsent(configType: ConfigType) {
        javaPlugin.saveResource(configType.fileName, false)
        loadConfig(configType)
    }

    @Throws(FileNotFoundException::class)
    fun getInputStream(configType: ConfigType): InputStream =
        javaPlugin.getResource(configType.fileName)
            ?: throw FileNotFoundException("The file with name \"${configType.fileName}\" could not be read.")


    fun loadConfig(configType: ConfigType) {
        try {
            loadedConfigs[configType] =
                (configType.tomlFileClass.constructors.find { it.parameters.size == 1 && it.parameters[0]::class.java == InputStream::class.java }
                    ?.call(getInputStream(configType)))!!
        } catch (e: Exception) {
            javaPlugin.logger.warning("Something has went wrong during when parsing the file ${configType.fileName}")
        }
    }

    fun saveAllIfAbsent() {
        ConfigType.values().forEach { saveIfAbsent(it) }
    }
}