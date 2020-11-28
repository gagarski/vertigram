package ski.gagar.vertigram.entities

data class Sticker(
    val fileId: String,
    val fileUniqueId: String,
    val width: Long,
    val height: Long,
    @get:JvmName("getIsAnimated")
    val isAnimated: Boolean,
    val thumb: PhotoSize? = null,
    val emoji: String? = null,
    val setName: String? = null,
    val maskPosition: MaskPosition? = null,
    val fileSize: Long? = null
)