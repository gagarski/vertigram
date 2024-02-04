package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.BotDescription

@TgMethod
data class GetMyDescription(
    val languageCode: String? = null
) : JsonTgCallable<BotDescription>()
