package ski.gagar.vxutil.vertigram.types

import java.time.Duration

data class InlineQueryResultMpeg4Gif(
    val id: String,
    val mpeg4Url: String,
    val mpeg4Width: Int? = null,
    val mpeg4Height: Int? = null,
    val mpeg4Duration: Duration? = null,
    val thumbnailUrl: String,
    val thumbnailMimeType: String? = null,
    val title: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.MPEG4_GIF
}
