package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type Voice.
 */
data class Voice(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Long,
    val mimeType: String? = null,
    val fileSize: Long? = null
)
