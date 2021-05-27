package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.entities.InlineQueryResult

@TgMethod
data class AnswerInlineQuery(
    val inlineQueryId: String,
    val results: List<InlineQueryResult>,
    val cacheTime: Long? = null,
    @get:JvmName("getIsPersonal")
    val isPersonal: Boolean = false, // Optional by specification, sent as false by default
    val nextOffset: String? = null,
    val switchPmText: String? = null,
    val switchPmParameter: String? = null
) : JsonTgCallable<Boolean>()
