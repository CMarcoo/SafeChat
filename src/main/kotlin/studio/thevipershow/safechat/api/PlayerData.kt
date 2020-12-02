package studio.thevipershow.safechat.api

/**
 * An Object that stores all of the
 * flags that the player has received.
 */
@Suppress("MemberVisibilityCanBePrivate")
data class PlayerData(val flags: Map<Check, Int>) {

    // val flags = EnumMap<Check, Int>(Check::class.java)

    /**
     * Increment a flag by 1 if it found.
     */
    fun incrementFlag(check: Check) {
        flags[check]?.inc()
    }
}