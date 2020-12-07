package me.thevipershow.safechat.api.data;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

@Getter
public final class PlayerData {

    @Deprecated
    public PlayerData(@NotNull String username, int words, int domain, int address, int repetition, int flood) {
        this.username = username;
        this.flagCount = new EnumMap<>(Flag.class);
        flagCount.put(Flag.WORDS, words);
        flagCount.put(Flag.DOMAIN, domain);
        flagCount.put(Flag.ADDRESS, address);
        flagCount.put(Flag.REPETITION, repetition);
        flagCount.put(Flag.FLOOD, flood);
    }

    public PlayerData(@NotNull String username, @NotNull Map<Flag, Integer> data) {
        this.username = username;
        this.flagCount = new EnumMap<>(Flag.class);
        this.flagCount.putAll(data);
    }

    public PlayerData(@NotNull String username) {
        this.username = username;
        this.flagCount = new EnumMap<>(Flag.class);
        for (Flag flag : Flag.values()) {
            this.flagCount.put(flag, 0);
        }
    }

    private final String username;
    private final Map<Flag, Integer> flagCount;

    /**
     * Increase a flag's value count.
     * @param flag The flag type.
     * @return The new value after it increased.
     */
    public final int increaseFlag(@NotNull Flag flag) {
        return this.flagCount.compute(flag, (k,v) -> v+=1);
    }

    /**
     * Get the count of a specific flag.
     * @param flag The flag type.
     * @return The flag amount.
     */
    public final int getFlag(@NotNull Flag flag) {
        return this.flagCount.get(flag);
    }
}
