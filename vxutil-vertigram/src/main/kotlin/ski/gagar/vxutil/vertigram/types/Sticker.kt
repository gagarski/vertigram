package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type Sticker.
 */
data class Sticker(
    val fileId: String,
    val fileUniqueId: String,
    val width: Long,
    val height: Long,
    @get:JvmName("getIsAnimated")
    val isAnimated: Boolean,
    @get:JvmName("getIsVideo")
    val isVideo: Boolean,
    val thumb: PhotoSize? = null,
    val emoji: String? = null,
    val setName: String? = null,
    val maskPosition: MaskPosition? = null,
    val fileSize: Long? = null
)
