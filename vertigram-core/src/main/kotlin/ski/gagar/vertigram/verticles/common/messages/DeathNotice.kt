package ski.gagar.vertigram.verticles.common.messages

/**
 * A death reason for [DeathNotice].
 */
enum class DeathReason {
    /**
     * A verticle has completed its job and no longer needed.
     */
    COMPLETED,

    /**
     * A verticle failed its job
     */
    FAILED,

    /**
     * A verticle reacted to cancel request and died
     */
    CANCELLED,

    /**
     * A verticle timed out
     */
    TIMEOUT
}

/**
 * Death notice for [ski.gagar.vertigram.verticles.common.AbstractHierarchyVerticle]
 */
data class DeathNotice(
    /**
     * Deployment id of the verticle
     */
    val id: String,
    /**
     * Reason of death
     */
    val reason: DeathReason
)
