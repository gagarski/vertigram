package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InputMediaVideo.
 */
data class InputMediaVideo(
    val media: String,
    val thumb: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val width: Long? = null,
    val height: Long? = null,
    val duration: Long? = null,
    val supportsStreaming: Boolean = false
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.VIDEO
}

val InputMediaVideo.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
