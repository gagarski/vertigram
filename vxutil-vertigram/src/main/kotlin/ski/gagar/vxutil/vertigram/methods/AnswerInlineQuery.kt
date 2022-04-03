package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.InlineQueryResult

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
