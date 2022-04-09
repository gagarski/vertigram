package ski.gagar.vxutil.vertigram.methods

data class AnswerPreCheckoutQuery(
    val preCheckoutQueryId: String,
    val ok: Boolean,
    val errorMessage: String? = null
) : JsonTgCallable<Boolean>()
