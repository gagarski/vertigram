package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

data class SetMyName(
    val name: String,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
