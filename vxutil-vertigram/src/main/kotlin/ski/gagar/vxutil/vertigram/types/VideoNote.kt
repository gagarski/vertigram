package ski.gagar.vxutil.vertigram.types

data class VideoNote(
    val fileId: String,
    val fileUniqueId: String,
    val length: Long,
    val duration: Long,
    val thumb: PhotoSize? = null,
    val fileSize: Long?
)
