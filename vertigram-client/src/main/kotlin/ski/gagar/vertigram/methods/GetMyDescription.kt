package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.BotDescription

data class GetMyDescription(
    val languageCode: String? = null
) : JsonTelegramCallable<BotDescription>()
