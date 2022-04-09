package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.types.attachments.Attachment

/**
 * Telegram type InputMediaVideo.
 */
data class InputMediaVideo(
    override val media: Attachment,
    override val thumb: Attachment? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val width: Long? = null,
    val height: Long? = null,
    val duration: Long? = null,
    val supportsStreaming: Boolean = false
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.VIDEO
    override fun instantiate(media: Attachment, thumb: Attachment?) = copy(media = media, thumb = thumb)
}

val InputMediaVideo.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
