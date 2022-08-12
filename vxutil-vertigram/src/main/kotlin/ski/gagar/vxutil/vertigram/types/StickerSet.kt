package ski.gagar.vxutil.vertigram.types

data class StickerSet(
    val name: String,
    val title: String,
    val stickerType: StickerType,
    @get:JvmName("getIsAnimated")
    val isAnimated: Boolean = false,
    @get:JvmName("getIsVideo")
    val isVideo: Boolean = false,
    val stickers: List<Sticker> = listOf(),
    val thumb: List<PhotoSize>? = null
)
