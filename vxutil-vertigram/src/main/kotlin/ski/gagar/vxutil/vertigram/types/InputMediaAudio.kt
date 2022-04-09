package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.types.attachments.Attachment

/**
 * Telegram type InputMediaAudio
 */
data class InputMediaAudio(
    override val media: Attachment,
    override val thumb: Attachment? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val duration: Long? = null,
    val performer: String? = null,
    val title: String? = null
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.AUDIO
    override fun instantiate(media: Attachment, thumb: Attachment?) = copy(media = media, thumb = thumb)
}

val InputMediaAudio.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
