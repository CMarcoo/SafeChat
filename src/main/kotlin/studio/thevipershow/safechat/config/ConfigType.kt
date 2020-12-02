package studio.thevipershow.safechat.config

import kotlin.reflect.KClass

enum class ConfigType(val fileName: String, val tomlFileClass: KClass<DatabaseFile>) {

    DATABASE("database.toml", DatabaseFile::class);

}
