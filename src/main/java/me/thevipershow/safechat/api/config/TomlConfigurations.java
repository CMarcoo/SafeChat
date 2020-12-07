package me.thevipershow.safechat.api.config;

import lombok.RequiredArgsConstructor;
import me.thevipershow.safechat.api.config.dbconfig.DatabaseTomlConfiguration;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum TomlConfigurations implements ConfigurationHolder<Class<? extends TomlAbstractConfiguration<?>>> {

    DATABASE_CONFIGURATION(DatabaseTomlConfiguration.class)
    ;

    private final Class<? extends TomlAbstractConfiguration<?>> theClass;

    @Override
    public @NotNull Class<? extends TomlAbstractConfiguration<?>> getConfigurationClass() {
        return theClass;
    }
}
