package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.InlineQueryResult
import ski.gagar.vertigram.types.SentWebAppMessage
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [answerWebAppQuery](https://core.telegram.org/bots/api#answerwebappquery) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class AnswerWebAppQuery(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val webAppQueryId: String,
    val result: List<InlineQueryResult>
) : JsonTelegramCallable<SentWebAppMessage>()
