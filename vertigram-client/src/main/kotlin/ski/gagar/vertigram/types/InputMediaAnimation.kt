package ski.gagar.vertigram.types

import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.types.richtext.HasOptionalRichCaption
import java.time.Duration

data class InputMediaAnimation(
    override val media: Attachment,
    override val thumbnail: Attachment? = null,
    override val caption: String? = null,
    override val parseMode: ParseMode? = null,
    override val captionEntities: List<MessageEntity>? = null,
    val width: Int? = null,
    val height: Int? = null,
    val duration: Duration? = null,
    // Since Telegram Bot API 6.4
    val hasSpoiler: Boolean = false
) : InputMedia, HasOptionalRichCaption {
    override val type: InputMediaType = InputMediaType.ANIMATION
    override fun instantiate(media: Attachment, thumbnail: Attachment?) = copy(media = media, thumbnail = thumbnail)
}
