package me.thevipershow.safechat.api.config;

import com.moandjiezana.toml.Toml;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTomlNode<T> extends AbstractNode<T> {

    protected final Toml toml;

    public abstract void setValueFromToml();

    public AbstractTomlNode(@NotNull String nodeKey, @NotNull Toml toml) {
        super(nodeKey);
        this.toml = toml;
        setValueFromToml();
    }

}
