package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InputMediaAudio
 */
data class InputMediaAudio(
    val media: String,
    val thumb: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val duration: Long? = null,
    val performer: String? = null,
    val title: String? = null
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.AUDIO
}

val InputMediaAudio.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
