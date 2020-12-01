package me.thevipershow.safechat.api

import java.util.*

/**
 * An Object that stores all of the
 * flags that the player has received.
 */
@Suppress("MemberVisibilityCanBePrivate")
object PlayerData {

    val flags = EnumMap<Check, Int>(Check::class.java)

    /**
     * Increment a flag by 1 if it found.
     */
    fun incrementFlag(check: Check) {
        flags.computeIfPresent(check) { _: Check, v: Int -> (v.inc()) }
    }
}