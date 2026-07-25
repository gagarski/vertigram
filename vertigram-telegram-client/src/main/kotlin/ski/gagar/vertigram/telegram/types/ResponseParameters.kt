package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Describes why a request was unsuccessful.
 *
 * See Telegram's [ResponseParameters](https://core.telegram.org/bots/api#responseparameters) documentation.
 */
@TelegramCodegen.Type
data class ResponseParameters internal constructor(
    /** Identifier of the supergroup to which the group was migrated. */
    val migrateToChatId: Long? = null,
    /** Time left before a request can be repeated after exceeding flood control. */
    val retryAfter: Duration? = null
) {
    companion object
}
