package me.thevipershow.safechat.api.config;

import org.jetbrains.annotations.NotNull;

public interface ConfigurationHolder<T extends Class<? extends AbstractConfiguration<?>>> {

    /**
     * Get the configuration file.
     * @return The ConfigurationFile.
     */
    @NotNull
    T getConfigurationClass();
}
