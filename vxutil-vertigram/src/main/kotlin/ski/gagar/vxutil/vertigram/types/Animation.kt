package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type [Animation](https://core.telegram.org/bots/api#animation).
 */
data class Animation(
    val fileId: String,
    val fileUniqueId: String,
    val width: Long,
    val height: Long,
    val duration: Long,
    val thumb: PhotoSize? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null
)
