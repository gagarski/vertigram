package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InputLocationMessageContent.
 */
data class InputLocationMessageContent(
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Long? = null,
    val heading: Long? = null,
    val proximityAlertRadius: Long? = null,
)
