package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.BotShortDescription

@TgMethod
data class GetMyShortDescription(
    val languageCode: String? = null
) : JsonTgCallable<BotShortDescription>()
