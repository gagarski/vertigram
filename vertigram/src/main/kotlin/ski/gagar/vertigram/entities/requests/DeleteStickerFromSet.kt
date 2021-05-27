package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod
data class DeleteStickerFromSet(
    val sticker: String
) : JsonTgCallable<Boolean>()

