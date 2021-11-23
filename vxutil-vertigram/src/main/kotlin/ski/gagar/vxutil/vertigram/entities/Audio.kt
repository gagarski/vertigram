package ski.gagar.vxutil.vertigram.entities

data class Audio(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Long,
    val performer: String? = null,
    val title: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
    val thumb: PhotoSize? = null
)
