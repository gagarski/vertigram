package ski.gagar.vertigram.types

data class ProximityAlertTriggered(
    val traveler: User,
    val watcher: User,
    val distance: Int
)
