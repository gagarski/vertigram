package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [ResponseParameters](https://core.telegram.org/bots/api#responseparameters) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class ResponseParameters internal constructor(
    val migrateToChatId: Long? = null,
    val retryAfter: Duration? = null
) {
    companion object
}