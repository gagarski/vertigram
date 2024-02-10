package ski.gagar.vertigram.methods

data class SetMyShortDescription(
    val shortDescription: String = "",
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
