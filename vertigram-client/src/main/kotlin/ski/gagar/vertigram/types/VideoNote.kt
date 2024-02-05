package ski.gagar.vertigram.types

import java.time.Duration

data class VideoNote(
    val fileId: String,
    val fileUniqueId: String,
    val length: Int,
    val duration: Duration,
    val thumbnail: PhotoSize? = null,
    val fileSize: Long? = null
)
