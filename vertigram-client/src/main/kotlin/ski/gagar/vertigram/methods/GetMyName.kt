package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.BotName

@TgMethod
data class GetMyName(
    val languageCode: String? = null
) : JsonTgCallable<BotName>()
