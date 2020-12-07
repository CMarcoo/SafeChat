package me.thevipershow.safechat.api.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface IConfiguration<R extends Enum<R> & INode<?>> {

    /**
     * Get the file that this configuration is currently stored in.
     * @return The File.
     */
    @NotNull
    File getConfigurationFile();

    /**
     * Get the value of a node
     * @param searchIndex The class of the nodes.
     * @param <T> The stored value of the node, null if the node isn't saved.
     * @return the node.
     */
    @Nullable
    <T extends INode<T>> T getNodeValue(R searchIndex);
}
