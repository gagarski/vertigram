package ski.gagar.vxutil.vertigram.types

import java.time.Duration

data class Audio(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Duration,
    val performer: String? = null,
    val title: String? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long? = null,
    val thumb: PhotoSize? = null
)
