package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.entities.InlineQueryResult

data class AnswerCallbackQuery(
    val callbackQueryId: String,
    val text: String? = null,
    val showAlert: Boolean? = null,
    val url: String? = null,
    val cacheTime: Long? = null
) : JsonTgCallable<Boolean>()