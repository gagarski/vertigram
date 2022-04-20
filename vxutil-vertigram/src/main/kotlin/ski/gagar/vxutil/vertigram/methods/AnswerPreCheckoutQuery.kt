package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod
data class AnswerPreCheckoutQuery(
    val preCheckoutQueryId: String,
    val ok: Boolean,
    val errorMessage: String? = null
) : JsonTgCallable<Boolean>()
