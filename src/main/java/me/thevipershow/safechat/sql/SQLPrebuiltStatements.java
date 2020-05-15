package me.thevipershow.safechat.sql;

public enum SQLPrebuiltStatements {

    SQLITE_CREATE_TABLE("CREATE TABLE IF NOT EXISTS safechat_data\n"
            + "(\n"
            + "\tplayer_uuid CHARACTER(36) NOT NULL UNIQUE PRIMARY KEY ,\n"
            + "\tflags INT NOT NULL);"),
    SQLITE_ADD_PLAYER_OR_UPDATE("INSERT INTO safechat_data (player_uuid, flags) VALUES (?,?) ON CONFLICT (player_uuid) DO UPDATE SET flags = safechat_data.flags + ?;"),
    SQLITE_GET_PLAYER_DATA("SELECT flags FROM safechat_data WHERE player_uuid = ?"),
    SQLITE_GET_TOP_DATA("SELECT player_uuid, flags FROM safechat_data ORDER BY flags LIMIT %d;"),
    POSTGRESQL_CREATE_TABLE("CREATE TABLE IF NOT EXISTS safechat_data\n"
            + "(\n"
            + "\tplayer_uuid UUID NOT NULL UNIQUE PRIMARY KEY,\n"
            + "\tflags INT NOT NULL);"),
    POSTGRESQL_ADD_PLAYER_OR_UPDATE("INSERT INTO safechat_data (player_uuid, flags) VALUES (?,?) ON CONFLICT (player_uuid) DO UPDATE SET flags = safechat_data.flags + ?;"),
    POSTGRESQL_GET_PLAYER_DATA("SELECT FLAGS FROM safechat_data WHERE player_uuid = ?;"),
    POSTGRESQL_GET_TOP_DATA("SELECT player_uuid, flags FROM safechat_data ORDER BY flags DESC LIMIT %d;"),
    MYSQL_CREATE_TABLE("CREATE TABLE IF NOT EXISTS safechat_data (\n" +
            "  player_uuid CHAR(36) UNIQUE PRIMARY KEY NOT NULL,\n" +
            "  flags INT NOT NULL\n" +
            ");"),
    MYSQL_ADD_PLAYER_OR_UPDATE("INSERT INTO\n" +
            "  safechat_data (player_uuid, flags)\n" +
            "VALUES\n" +
            "  (?, ?) ON DUPLICATE KEY\n" +
            "UPDATE\n" +
            "  flags = safechat_data.flags + ?;\n");

    SQLPrebuiltStatements(String SQL) {
        this.SQL = SQL;
    }

    final String SQL;

    public String getSQL() {
        return SQL;
    }

    public String formatSQL(Object o) {
        return String.format(SQL, o);
    }
}
