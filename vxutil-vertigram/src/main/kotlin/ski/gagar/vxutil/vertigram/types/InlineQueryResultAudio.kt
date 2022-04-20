package ski.gagar.vxutil.vertigram.types

import java.time.Duration

data class InlineQueryResultAudio(
    val id: String,
    val audioUrl: String,
    val title: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val performer: String? = null,
    val audioDuration: Duration? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.AUDIO
}
