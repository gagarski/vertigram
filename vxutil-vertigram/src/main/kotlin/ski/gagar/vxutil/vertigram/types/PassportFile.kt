package ski.gagar.vxutil.vertigram.types

import java.time.Instant

/**
 * Telegram type PassportFile.
 */
data class PassportFile(
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Long,
    val fileDate: Instant
)
