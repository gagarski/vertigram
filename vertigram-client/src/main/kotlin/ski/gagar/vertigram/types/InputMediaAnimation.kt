package ski.gagar.vertigram.types

import ski.gagar.vertigram.types.attachments.Attachment
import java.time.Duration

data class InputMediaAnimation(
    override val media: Attachment,
    override val thumbnail: Attachment? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val width: Int? = null,
    val height: Int? = null,
    val duration: Duration? = null,
    // Since Telegram Bot API 6.4
    val hasSpoiler: Boolean = false
) : InputMedia {
    override val type: InputMediaType = InputMediaType.ANIMATION
    override fun instantiate(media: Attachment, thumbnail: Attachment?) = copy(media = media, thumbnail = thumbnail)
}