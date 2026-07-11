package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InlineQuery
import ski.gagar.vertigram.telegram.types.SentGuestMessage

/**
 * Telegram [answerGuestQuery](https://core.telegram.org/bots/api#answerguestquery) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class AnswerGuestQuery internal constructor(
    val guestQueryId: String,
    val result: InlineQuery.Result
) : JsonTelegramCallable<SentGuestMessage>()
