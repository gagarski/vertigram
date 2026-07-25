package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Represents a result of an inline query that was chosen by the user and sent to their chat partner.
 *
 * See Telegram's [ChosenInlineResult](https://core.telegram.org/bots/api#choseninlineresult) documentation.
 */
@TelegramCodegen.Type
data class ChosenInlineResult internal constructor(
    /** The unique identifier for the result that was chosen. */
    val resultId: String,
    /** The user that chose the result. */
    val from: User,
    /** Sender location, only for bots that require user location. */
    val location: Location? = null,
    /**
     * Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message.
     * Will also be received in callback queries and can be used to edit the message.
     */
    val inlineMessageId: String? = null,
    /** The query that was used to obtain the result. */
    val query: String
) {
    companion object
}
