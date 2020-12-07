package me.thevipershow.safechat.api.config;

import com.moandjiezana.toml.Toml;
import org.jetbrains.annotations.NotNull;

public final class IntegerTomlNode extends AbstractTomlNode<Long> {

    public IntegerTomlNode(@NotNull String nodeKey, @NotNull Toml toml) {
        super(nodeKey, toml);
    }

    @Override
    public final void setValueFromToml() {
        super.setValue(super.toml.getLong(super.getKey()));
    }
}
