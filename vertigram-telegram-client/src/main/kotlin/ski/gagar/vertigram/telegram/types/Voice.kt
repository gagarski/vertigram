package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [Voice](https://core.telegram.org/bots/api#voice) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class Voice internal constructor(
    val fileId: String,
    val fileUniqueId: String,
    val duration: Duration,
    val mimeType: String? = null,
    val fileSize: Long? = null
) {
    companion object
}
