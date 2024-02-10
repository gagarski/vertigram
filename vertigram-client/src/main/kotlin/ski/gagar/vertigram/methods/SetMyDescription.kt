package ski.gagar.vertigram.methods

data class SetMyDescription(
    val description: String = "",
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
