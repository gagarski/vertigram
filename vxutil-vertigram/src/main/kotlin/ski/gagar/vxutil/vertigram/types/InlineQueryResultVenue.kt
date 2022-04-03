package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineQueryResultVenue
 */
data class InlineQueryResultVenue(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val foursquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
    val thumbUrl: String? = null,
    val thumbWidth: Long? = null,
    val thumbHeight: Long? = null
) : InlineQueryResult() {
    override val type: InlineQueryResultType = InlineQueryResultType.VENUE
}
