package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [ResponseParameters](https://core.telegram.org/bots/api#responseparameters) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ResponseParameters(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val migrateToChatId: Long? = null,
    val retryAfter: Duration? = null
)
