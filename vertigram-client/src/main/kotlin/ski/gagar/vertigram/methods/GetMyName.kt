package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.BotName

data class GetMyName(
    val languageCode: String? = null
) : JsonTelegramCallable<BotName>()
