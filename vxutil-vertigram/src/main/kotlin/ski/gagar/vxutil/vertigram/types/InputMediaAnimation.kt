package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InputMediaAnimation
 */
data class InputMediaAnimation(
    val media: String,
    val thumb: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val width: Long? = null,
    val height: Long? = null,
    val duration: Long? = null
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.ANIMATION
}

val InputMediaAnimation.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
