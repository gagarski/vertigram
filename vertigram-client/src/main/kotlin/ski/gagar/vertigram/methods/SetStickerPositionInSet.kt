package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

data class SetStickerPositionInSet(
    val sticker: String,
    val position: Int
) : JsonTelegramCallable<Boolean>()

