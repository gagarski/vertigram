package ski.gagar.vxutil.vertigram.types

import java.time.Duration

data class VideoNote(
    val fileId: String,
    val fileUniqueId: String,
    val length: Int,
    val duration: Duration,
    val thumb: PhotoSize? = null,
    val fileSize: Long? = null
)
