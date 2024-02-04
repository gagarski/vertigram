package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod
data class SetStickerPositionInSet(
    val sticker: String,
    val position: Int
) : JsonTgCallable<Boolean>()

