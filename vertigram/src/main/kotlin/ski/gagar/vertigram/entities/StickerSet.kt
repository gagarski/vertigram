package ski.gagar.vertigram.entities

data class StickerSet(
    val name: String,
    val title: String,
    @get:JvmName("getIsAnimated")
    val isAnimated: Boolean,
    val containsMasks: Boolean,
    val stickers: List<Sticker> = listOf(),
    val thumb: List<PhotoSize> = listOf()
)
