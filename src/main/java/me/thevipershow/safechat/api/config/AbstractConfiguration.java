package me.thevipershow.safechat.api.config;

import lombok.Getter;
import me.thevipershow.safechat.SafeChatPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public abstract class AbstractConfiguration<R extends Enum<R> & INode<?>> implements IConfiguration<R> {

    @Getter
    private final String filename;
    private final File file;

    public AbstractConfiguration(@NotNull String filename, @NotNull SafeChatPlugin safeChatPlugin) {
        this.filename = filename;
        File file = new File(safeChatPlugin.getDataFolder(), filename);
        if (file.isDirectory() || !file.exists() || !file.canRead()) {
            throw new IllegalArgumentException(String.format("The specified file %s is not accessible by this plugin.", filename));
        }
        this.file = file;
    }

    public abstract void loadNodes();

    /**
     * Get the file that this configuration is currently stored in.
     *
     * @return The File.
     */
    @Override
    public @NotNull File getConfigurationFile() {
        return this.file;
    }

}
