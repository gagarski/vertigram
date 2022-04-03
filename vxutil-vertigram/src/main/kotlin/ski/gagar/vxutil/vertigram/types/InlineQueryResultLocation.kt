package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineQueryResultLocation
 */
data class InlineQueryResultLocation(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Long? = null,
    val heading: Long? = null,
    val proximityAlertRadius: Long? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
    val thumbUrl: String? = null,
    val thumbWidth: Long? = null,
    val thumbHeight: Long? = null
    ) : InlineQueryResult() {
    override val type: InlineQueryResultType = InlineQueryResultType.LOCATION
}
