package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InlineQuery
import ski.gagar.vertigram.telegram.types.SentGuestMessage

/**
 * Use this method to reply to a received guest message. On success, a [SentGuestMessage] object is returned.
 *
 * See Telegram's [answerGuestQuery](https://core.telegram.org/bots/api#answerguestquery) documentation.
 */
@TelegramCodegen.Method
data class AnswerGuestQuery internal constructor(
    /** Unique identifier for the query to be answered. */
    val guestQueryId: String,
    /** Object describing the message to be sent. */
    val result: InlineQuery.Result
) : JsonTelegramCallable<SentGuestMessage>()
