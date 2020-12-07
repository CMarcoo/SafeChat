package me.thevipershow.safechat.api.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Flag {

    /**
     * A word that is not allowed by SafeChat.
     */
    WORDS("Words"),

    /**
     * A domain that is not allowed by SafeChat.
     */
    DOMAIN("Domain"),

    /**
     * An IPv4\IPv6 address that is not allowed by SafeChat.
     */
    ADDRESS("Address"),

    /**
     * A repetition of messages that are too similar.
     */
    REPETITION("Repetition"),

    /**
     * Messages sent too fast.
     */
    FLOOD("Flood");

    private final String name;
}
