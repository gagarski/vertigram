package ski.gagar.vxutil.vertigram.types
data class Sticker(
    val fileId: String,
    val fileUniqueId: String,
    val type: StickerType,
    val width: Int,
    val height: Int,
    @get:JvmName("getIsAnimated")
    val isAnimated: Boolean = false,
    @get:JvmName("getIsVideo")
    val isVideo: Boolean = false,
    val thumbnail: PhotoSize? = null,
    val emoji: String? = null,
    val setName: String? = null,
    val premiumAnimation: File? = null,
    val maskPosition: MaskPosition? = null,
    val customEmojiId: String? = null,
    val fileSize: Long? = null,
    // Since Bot API 6.6
    val needsRepainting: Boolean = false
)
