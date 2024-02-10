package ski.gagar.vertigram.methods

data class SetMyName(
    val name: String,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
