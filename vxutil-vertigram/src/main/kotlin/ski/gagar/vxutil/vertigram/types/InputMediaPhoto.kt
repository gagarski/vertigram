package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.types.attachments.Attachment

data class InputMediaPhoto(
    override val media: Attachment,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null
) : InputMedia {
    override val type: InputMediaType = InputMediaType.PHOTO
    override val thumb = null
    override fun instantiate(media: Attachment, thumb: Attachment?) = copy(media = media)
}

val InputMediaPhoto.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
