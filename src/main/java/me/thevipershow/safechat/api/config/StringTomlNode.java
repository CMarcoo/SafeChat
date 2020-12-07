package me.thevipershow.safechat.api.config;

import com.moandjiezana.toml.Toml;
import org.jetbrains.annotations.NotNull;

public final class StringTomlNode extends AbstractTomlNode<String> {
    public StringTomlNode(@NotNull String nodeKey, @NotNull Toml toml) {
        super(nodeKey, toml);
    }

    @Override
    public final void setValueFromToml() {
        setValue(super.toml.getString(super.getKey()));
    }
}
