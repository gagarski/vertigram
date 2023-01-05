package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.types.attachments.Attachment

data class InputMediaPhoto(
    override val media: Attachment,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    // Since Telegram Bot API 6.4
    val hasSpoiler: Boolean = false
) : InputMedia {
    override val type: InputMediaType = InputMediaType.PHOTO
    override val thumb = null
    override fun instantiate(media: Attachment, thumb: Attachment?) = copy(media = media)
}
