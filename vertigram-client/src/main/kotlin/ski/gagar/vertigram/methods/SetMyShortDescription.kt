package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

data class SetMyShortDescription(
    val shortDescription: String = "",
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
