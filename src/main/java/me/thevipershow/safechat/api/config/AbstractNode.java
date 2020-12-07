package me.thevipershow.safechat.api.config;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractNode<T> implements INode<T> {

    private T t;
    private final String nodeKey;

    public AbstractNode(@NotNull String nodeKey, @NotNull T t) {
        this.t = t;
        this.nodeKey = nodeKey;
    }

    public AbstractNode(@NotNull String nodeKey) {
        this.nodeKey = nodeKey;
    }

    /**
     * Get the key of this node.
     *
     * @return The key.
     */
    @Override
    public @NotNull String getKey() {
        return nodeKey;
    }

    /**
     * Get the value of this node.
     *
     * @return The value.
     */
    @Override
    public @NotNull T getValue() {
        return t;
    }

    /**
     * Set the value of T
     *
     * @param t The type value.
     */
    @Override
    public void setValue(@NotNull T t) {
        this.t = t;
    }
}
