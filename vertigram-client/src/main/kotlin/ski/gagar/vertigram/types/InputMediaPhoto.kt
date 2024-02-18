package ski.gagar.vertigram.types

import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.types.richtext.HasOptionalRichCaption

data class InputMediaPhoto(
    override val media: Attachment,
    override val caption: String? = null,
    override val parseMode: ParseMode? = null,
    override val captionEntities: List<MessageEntity>? = null,
    // Since Telegram Bot API 6.4
    val hasSpoiler: Boolean = false
) : InputMedia, HasOptionalRichCaption {
    override val type: InputMediaType = InputMediaType.PHOTO
    override val thumbnail = null
    override fun instantiate(media: Attachment, thumbnail: Attachment?) = copy(media = media)
}
