package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.InlineQueryResult
import ski.gagar.vxutil.vertigram.types.InlineQueryResultsButton
import java.time.Duration

@TgMethod
data class AnswerInlineQuery(
    val inlineQueryId: String,
    val results: List<InlineQueryResult>,
    val cacheTime: Duration? = null,
    @get:JvmName("getIsPersonal")
    val isPersonal: Boolean = false,
    val nextOffset: String? = null,
    // Since Telegram Bot API 6.7
    val button: InlineQueryResultsButton? = null
) : JsonTgCallable<Boolean>()
