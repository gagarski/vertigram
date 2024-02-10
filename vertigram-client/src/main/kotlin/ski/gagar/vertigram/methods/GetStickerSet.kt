package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.StickerSet

data class GetStickerSet(
    val name: String
) : JsonTelegramCallable<StickerSet>()
