package ski.gagar.vxutil.vertigram.types

import java.time.Duration

data class InlineQueryResultVideo(
    val id: String,
    val videoUrl: String,
    val mimeType: String,
    val thumbUrl: String,
    val title: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val videoWidth: Int? = null,
    val videoHeight: Int? = null,
    val videoDuration: Duration? = null,
    val description: String? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.VIDEO
}

val InlineQueryResultVideo.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
