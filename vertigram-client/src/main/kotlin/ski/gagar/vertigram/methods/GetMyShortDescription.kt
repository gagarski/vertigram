package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.BotShortDescription

data class GetMyShortDescription(
    val languageCode: String? = null
) : JsonTelegramCallable<BotShortDescription>()
