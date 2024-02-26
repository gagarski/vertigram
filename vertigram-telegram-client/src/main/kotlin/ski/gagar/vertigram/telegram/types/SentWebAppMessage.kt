package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [SentWebAppMessage](https://core.telegram.org/bots/api#sentwebappmessage) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SentWebAppMessage(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val inlineMessageId: String
)
