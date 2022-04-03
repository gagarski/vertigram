package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type Audio.
 */
data class Audio(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Long,
    val performer: String? = null,
    val title: String? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
    val thumb: PhotoSize? = null
)
