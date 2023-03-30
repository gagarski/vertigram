package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod
data class SetMyShortDescription(
    val shortDescription: String = "",
    val languageCode: String? = null
) : JsonTgCallable<Boolean>()
