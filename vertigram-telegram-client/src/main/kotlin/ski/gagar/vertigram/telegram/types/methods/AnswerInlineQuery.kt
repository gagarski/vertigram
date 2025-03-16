package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InlineQuery
import java.time.Duration

/**
 * Telegram [answerInlineQuery](https://core.telegram.org/bots/api#answerinlinequery) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class AnswerInlineQuery internal constructor(
    val inlineQueryId: String,
    val results: List<InlineQuery.Result>,
    val cacheTime: Duration? = null,
    @get:JvmName("getIsPersonal")
    val isPersonal: Boolean = false,
    val nextOffset: String? = null,
    val button: InlineQuery.Result.Button? = null
) : JsonTelegramCallable<Boolean>()