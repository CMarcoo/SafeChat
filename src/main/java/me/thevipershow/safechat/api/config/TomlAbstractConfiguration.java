package me.thevipershow.safechat.api.config;

import com.moandjiezana.toml.Toml;
import lombok.Getter;
import me.thevipershow.safechat.SafeChatPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class TomlAbstractConfiguration<T extends Enum<T> & EnumTomlNode> extends AbstractConfiguration<T> {

    @Getter
    protected final Toml toml;
    @Getter
    protected final Map<T, AbstractTomlNode<?>> tomlNodes = new HashMap<>();
    protected final T[] enumValues;

    @Override
    public void loadNodes() {
        try {
            for (T t : enumValues) {
                tomlNodes.putIfAbsent(t, t.getValue().getConstructor(String.class, Toml.class).newInstance(t.getKey(), this.toml));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TomlAbstractConfiguration(@NotNull String filename, @NotNull SafeChatPlugin safeChatPlugin, @NotNull Class<? extends T> enumClass) {
        super(filename, safeChatPlugin);
        this.toml = new Toml().read(super.getConfigurationFile());
        this.enumValues = enumClass.getEnumConstants();
        this.loadNodes();
    }

}
