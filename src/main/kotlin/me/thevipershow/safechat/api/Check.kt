package me.thevipershow.safechat.api

/**
 * These are all of the checks that this plugin is able
 * to recognize.
 */
enum class Check {

    /**
     * A word that is not allowed by SafeChat.
     */
    WORDS,

    /**
     * A domain that is not allowed by SafeChat.
     */
    DOMAIN,

    /**
     * An IPv4\IPv6 address that is not allowed by SafeChat.
     */
    ADDRESS,

    /**
     * A repetition of messages that are too similar.
     */
    REPETITION,

    /**
     * Messages sent too fast.
     */
    FLOOD;
}