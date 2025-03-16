package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InlineQuery
import ski.gagar.vertigram.telegram.types.SentWebAppMessage

/**
 * Telegram [answerWebAppQuery](https://core.telegram.org/bots/api#answerwebappquery) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class AnswerWebAppQuery internal constructor(
    val webAppQueryId: String,
    val result: List<InlineQuery.Result>
) : JsonTelegramCallable<SentWebAppMessage>()
