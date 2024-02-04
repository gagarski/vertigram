package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.StickerSet

@TgMethod
data class GetStickerSet(
    val name: String
) : JsonTgCallable<StickerSet>()
