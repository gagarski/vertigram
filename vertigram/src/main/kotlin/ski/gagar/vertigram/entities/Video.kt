package ski.gagar.vertigram.entities

data class Video(
    val fileId: String,
    val fileUniqueId: String,
    val width: Long,
    val height: Long,
    val duration: Long,
    val thumb: PhotoSize? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null
)