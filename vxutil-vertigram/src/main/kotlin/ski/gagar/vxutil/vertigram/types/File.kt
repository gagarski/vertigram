package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type File.
 */
data class File(
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Long? = null,
    val filePath: String? = null
)
