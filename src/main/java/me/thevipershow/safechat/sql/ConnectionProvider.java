package me.thevipershow.safechat.sql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionProvider {
    Connection findConnection() throws SQLException;
}
