package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type Location.
 */
data class Location(
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Int? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int? = null,
)
