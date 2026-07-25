package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Describes an inline message sent by a Web App on behalf of a user.
 *
 * See Telegram's [SentWebAppMessage](https://core.telegram.org/bots/api#sentwebappmessage) documentation.
 */
@TelegramCodegen.Type
data class SentWebAppMessage internal constructor(
    /** Identifier of the sent inline message, available when the message has an inline keyboard. */
    val inlineMessageId: String
) {
    companion object
}
