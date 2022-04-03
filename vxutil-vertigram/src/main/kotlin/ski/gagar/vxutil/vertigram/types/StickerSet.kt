package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type StickerSet.
 */
data class StickerSet(
    val name: String,
    val title: String,
    @get:JvmName("getIsAnimated")
    val isAnimated: Boolean,
    @get:JvmName("getIsVideo")
    val isVideo: Boolean,
    val containsMasks: Boolean,
    val stickers: List<Sticker>,
    val thumb: List<PhotoSize>? = null
)
