package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InlineQuery
import ski.gagar.vertigram.telegram.types.SentWebAppMessage

/**
 * Use this method to set the result of an interaction with a Web App and send a corresponding message on behalf of the
 * user to the chat from which the query originated. On success, a [SentWebAppMessage] object is returned.
 *
 * See Telegram's [answerWebAppQuery](https://core.telegram.org/bots/api#answerwebappquery) documentation.
 */
@TelegramCodegen.Method
data class AnswerWebAppQuery internal constructor(
    /** Unique identifier for the query to be answered. */
    val webAppQueryId: String,
    /** Objects describing the messages to be sent. */
    val result: List<InlineQuery.Result>
) : JsonTelegramCallable<SentWebAppMessage>()
