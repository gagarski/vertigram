package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ProximityAlertTriggered
 */
data class ProximityAlertTriggered(
    val traveler: User,
    val watcher: User,
    val distance: Long
)
