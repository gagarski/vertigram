package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Duration

/**
 * Telegram [LivePhoto](https://core.telegram.org/bots/api#livephoto) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class LivePhoto internal constructor(
    val photo: List<PhotoSize>? = null,
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val duration: Duration,
    val mimeType: String? = null,
    val fileSize: Long? = null
) {
    companion object
}
