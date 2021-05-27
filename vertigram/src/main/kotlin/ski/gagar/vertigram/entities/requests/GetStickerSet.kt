package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.entities.StickerSet

@TgMethod
data class GetStickerSet(
    val name: String
) : JsonTgCallable<StickerSet>()
