package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.InlineQueryResult
import ski.gagar.vertigram.types.InlineQueryResultsButton
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [answerInlineQuery](https://core.telegram.org/bots/api#answerinlinequery) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class AnswerInlineQuery(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val inlineQueryId: String,
    val results: List<InlineQueryResult>,
    val cacheTime: Duration? = null,
    @get:JvmName("getIsPersonal")
    val isPersonal: Boolean = false,
    val nextOffset: String? = null,
    val button: InlineQueryResultsButton? = null
) : JsonTelegramCallable<Boolean>()
