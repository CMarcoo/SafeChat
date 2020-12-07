package me.thevipershow.safechat.api.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface INode<T> {

    /**
     * Get the key of this node.
     * @return The key.
     */
    @NotNull
    String getKey();

    /**
     * Get the value of this node.
     * @return The value.
     */
    @Nullable
    T getValue();

    /**
     * Set the value of T
     * @param t The type value.
     */
    void setValue(@NotNull T t);
}
