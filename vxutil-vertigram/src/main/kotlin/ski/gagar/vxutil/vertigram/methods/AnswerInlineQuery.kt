package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.InlineQueryResult
import java.time.Duration

@TgMethod
data class AnswerInlineQuery(
    val inlineQueryId: String,
    val results: List<InlineQueryResult>,
    val cacheTime: Duration? = null,
    @get:JvmName("getIsPersonal")
    val isPersonal: Boolean = false,
    val nextOffset: String? = null,
    val switchPmText: String? = null,
    val switchPmParameter: String? = null
) : JsonTgCallable<Boolean>()
