package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [SentWebAppMessage](https://core.telegram.org/bots/api#sentwebappmessage) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class SentWebAppMessage internal constructor(
    val inlineMessageId: String
) {
    companion object
}
