package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import java.time.Duration

@TgMethod
data class AnswerCallbackQuery(
    val callbackQueryId: String,
    val text: String? = null,
    val showAlert: Boolean? = null,
    val url: String? = null,
    val cacheTime: Duration? = null
) : JsonTgCallable<Boolean>()
