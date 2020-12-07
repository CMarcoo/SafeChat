package me.thevipershow.safechat.api.config.dbconfig;

import me.thevipershow.safechat.SafeChatPlugin;
import me.thevipershow.safechat.api.config.INode;
import me.thevipershow.safechat.api.config.TomlAbstractConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DatabaseTomlConfiguration extends TomlAbstractConfiguration<DatabaseEnumTomlNode> {

    public DatabaseTomlConfiguration(@NotNull SafeChatPlugin safeChatPlugin) {
        super("database.toml", safeChatPlugin, DatabaseEnumTomlNode.class);
    }

    @Override
    public final <T extends INode<T>> @Nullable T getNodeValue(DatabaseEnumTomlNode searchIndex) {
        return (T) tomlNodes.get(searchIndex);
    }
}

