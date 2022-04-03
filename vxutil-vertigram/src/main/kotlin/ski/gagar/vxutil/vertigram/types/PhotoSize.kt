package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type PhotoSize.
 */
data class PhotoSize(
    val fileId: String,
    val fileUniqueId: String,
    val width: Long,
    val height: Long,
    val fileSize: Long? = null
)
