package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

data class SetMyDescription(
    val description: String = "",
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
