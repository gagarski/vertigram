package ski.gagar.vertigram.types

import java.time.Duration

data class Voice(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Duration,
    val mimeType: String? = null,
    val fileSize: Long? = null
)
