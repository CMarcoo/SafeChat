package me.thevipershow.safechat.api.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.thevipershow.safechat.SafeChatPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConfigManager {

    private static ConfigManager instance = null;
    private final SafeChatPlugin safeChatPlugin;

    private final HashMap<ConfigurationHolder<Class<? extends TomlAbstractConfiguration<?>>>, TomlAbstractConfiguration<?>> configMap = new HashMap<>();

    public static ConfigManager getInstance(@NotNull SafeChatPlugin safeChatPlugin) {
        if (instance == null) {
            instance = new ConfigManager(Objects.requireNonNull(safeChatPlugin, "The plugin was null!"));
        }
        return instance;
    }

    public final <T extends Enum<T> & ConfigurationHolder<Class<? extends TomlAbstractConfiguration<?>>>> void loadAllConfigs(Class<? extends T> epicGenerics) {
        for (T enumConstant : epicGenerics.getEnumConstants()) {
            this.loadConfig(enumConstant);
        }
    }

    public final void loadConfig(@NotNull ConfigurationHolder<Class<? extends TomlAbstractConfiguration<?>>> classConfigurationHolder) {
        final Class<? extends TomlAbstractConfiguration<?>> theClass = classConfigurationHolder.getConfigurationClass();
        try {
            Objects.requireNonNull(theClass.getConstructor(SafeChatPlugin.class).newInstance(this.safeChatPlugin), "Could not build " + theClass.getSimpleName());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
