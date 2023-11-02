package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.BotName

@TgMethod
data class GetMyName(
    val languageCode: String? = null
) : JsonTgCallable<BotName>()
