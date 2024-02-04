package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.BotShortDescription

@TgMethod
data class GetMyShortDescription(
    val languageCode: String? = null
) : JsonTgCallable<BotShortDescription>()
