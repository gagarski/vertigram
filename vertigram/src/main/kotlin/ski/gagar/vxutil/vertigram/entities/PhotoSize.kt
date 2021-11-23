package ski.gagar.vxutil.vertigram.entities

data class PhotoSize(
    val fileId: String,
    val fileUniqueId: String,
    val width: Long,
    val height: Long,
    val fileSize: Long? = null
)
