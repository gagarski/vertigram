package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import java.time.Duration

data class InputMediaAnimation(
    override val media: Attachment,
    override val thumb: Attachment? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val width: Int? = null,
    val height: Int? = null,
    val duration: Duration? = null
) : InputMedia {
    override val type: InputMediaType = InputMediaType.ANIMATION
    override fun instantiate(media: Attachment, thumb: Attachment?) = copy(media = media, thumb = thumb)
}
