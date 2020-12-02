package studio.thevipershow.safechat.config

import com.moandjiezana.toml.Toml
import java.io.File
import java.io.InputStream

abstract class TomlFile {

    constructor(file: File) {
        this.tomlValues = HashMap()
        this.tomlFile = Toml().read(file)
    }

    constructor(inputStream: InputStream) {
        this.tomlValues = HashMap()
        this.tomlFile = Toml().read(inputStream)
    }

    val tomlValues: HashMap<String, Any>

    protected val tomlFile: Toml
    abstract fun readValues()

}

enum class Type {

    STRING {
        override fun read(toml: Toml, key: String): Any = toml.getString(key)
    },
    BOOL {
        override fun read(toml: Toml, key: String): Any = toml.getBoolean(key)
    },
    LONG {
        override fun read(toml: Toml, key: String): Any = toml.getLong(key)
    };

    abstract fun read(toml: Toml, key: String): Any
}
