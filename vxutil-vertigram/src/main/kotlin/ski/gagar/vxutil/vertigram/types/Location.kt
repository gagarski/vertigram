package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type Location.
 */
data class Location(
    val longitude: Double,
    val latitude: Double,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Long? = null,
    val heading: Long? = null,
    val proximityAlertRadius: Long? = null,
)
