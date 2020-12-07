package me.thevipershow.safechat.api.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EnumTomlNode extends INode<Class<? extends AbstractTomlNode<?>>> {

    /**
     * Get the key of this node.
     *
     * @return The key.
     */
    @Override
    @NotNull String getKey();

    /**
     * Get the value of this node.
     *
     * @return The value.
     */
    @Override
    @Nullable Class<? extends AbstractTomlNode<?>> getValue();

    /**
     * This should NOT be overriden.
     * @param aClass the class.
     */
    @Override
    default void setValue(@NotNull Class<? extends AbstractTomlNode<?>> aClass) {
        throw new UnsupportedOperationException("Enum values should be constant.");
    }
}
