package me.thevipershow.safechat.api.config.dbconfig;

import lombok.RequiredArgsConstructor;
import me.thevipershow.safechat.api.config.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public enum DatabaseEnumTomlNode implements EnumTomlNode {

    ADDRESS("database.address", StringTomlNode.class),
    PORT("database.port", IntegerTomlNode.class),
    DATABASE_NAME("database.db-name", StringTomlNode.class),
    SQL_DIALECT("database.sql-dialect", StringTomlNode.class),
    USERNAME("database.username", StringTomlNode.class),
    PASSWORD("database.password", StringTomlNode.class),
    TIMEOUT("database.timeout", IntegerTomlNode.class);

    private final String key;
    private final Class<? extends AbstractTomlNode<?>> nodeClass;


    /**
     * Get the key of this node.
     *
     * @return The key.
     */
    @Override
    public @NotNull String getKey() {
        return key;
    }

    /**
     * Get the value of this node.
     *
     * @return The value.
     */
    @Override
    public @Nullable Class<? extends AbstractTomlNode<?>> getValue() {
        return nodeClass;
    }
}
