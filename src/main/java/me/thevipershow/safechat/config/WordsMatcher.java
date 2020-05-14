package me.thevipershow.safechat.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("WordsMatcher")
public final class WordsMatcher implements ConfigurationSerializable, Cloneable {
    private final long serialVersionUID = 1L;
    private final String pattern;
    private final String replace;

    public WordsMatcher(String pattern, String replace) {
        this.pattern = pattern;
        this.replace = replace;
    }

    public final String getPattern() {
        return pattern;
    }

    public final String getReplace() {
        return replace;
    }

    public final Pattern getCompiledPattern() {
        return Pattern.compile(this.pattern);
    }

    // Can throw NullPointerException
    @Override
    public final Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("pattern", Objects.requireNonNull(pattern));
        result.put("replace", replace);
        return result;
    }

    public static WordsMatcher deserialize(Map<String, Object> objectMap) {
        final String pattern = (String) objectMap.get("pattern");
        final String replace = (String) objectMap.get("replace");
        final boolean sendWarning = (boolean) objectMap.get("send-warning");
        return new WordsMatcher(pattern, replace);
    }
}
