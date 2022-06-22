package ski.gagar.vxutil.vertigram.types
data class Sticker(
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    @get:JvmName("getIsAnimated")
    val isAnimated: Boolean = false,
    @get:JvmName("getIsVideo")
    val isVideo: Boolean = false,
    val thumb: PhotoSize? = null,
    val emoji: String? = null,
    val setName: String? = null,
    val maskPosition: MaskPosition? = null,
    val fileSize: Long? = null,
    val premiumAnimation: File? = null
)
