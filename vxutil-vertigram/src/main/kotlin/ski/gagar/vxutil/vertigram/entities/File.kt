package ski.gagar.vxutil.vertigram.entities

data class File(
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Long? = null,
    val filePath: String? = null
)
