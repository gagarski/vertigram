package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [PreparedInlineMessage](https://core.telegram.org/bots/api#preparedinlinemessage) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class PreparedInlineMessage internal constructor(
    val id: String,
    val expirationDate: Instant
) {
    companion object
}
