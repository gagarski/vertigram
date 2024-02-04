package ski.gagar.vertigram.types

import java.time.Duration

data class InlineQueryResultLocation(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Duration? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
    val thumbnailUrl: String? = null,
    val thumbnailWidth: Int? = null,
    val thumbnailHeight: Int? = null
    ) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.LOCATION
}
