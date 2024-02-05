package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod
data class DeleteStickerFromSet(
    val sticker: String
) : JsonTgCallable<Boolean>()

