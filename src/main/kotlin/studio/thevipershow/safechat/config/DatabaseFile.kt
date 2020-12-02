package studio.thevipershow.safechat.config

import java.io.File
import java.io.InputStream

private const val baseNode = "database."

class DatabaseFile : TomlFile {

    constructor(file: File) : super(file)

    constructor(inputStream: InputStream) : super(inputStream)

    override fun readValues() {
        for (o in DatabaseValue.values()) super.tomlValues[o.key] = o.returnType.read(super.tomlFile, o.key)
    }

}

enum class DatabaseValue(val key: String, val returnType: Type) {
    ADDRESS("${baseNode}address", Type.STRING),
    PORT("${baseNode}port", Type.LONG),
    DATABASE_NAME("${baseNode}db-name", Type.STRING),
    SQL_DIALECT("${baseNode}sql-dialect", Type.STRING),
    USERNAME("${baseNode}username", Type.STRING),
    PASSWORD("${baseNode}password", Type.STRING),
    TIMEOUT("${baseNode}timeout", Type.LONG)
}
