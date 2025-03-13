package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InlineQuery
import ski.gagar.vertigram.telegram.types.SentWebAppMessage
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [answerWebAppQuery](https://core.telegram.org/bots/api#answerwebappquery) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
data class AnswerWebAppQuery(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val webAppQueryId: String,
    val result: List<InlineQuery.Result>
) : JsonTelegramCallable<SentWebAppMessage>()
