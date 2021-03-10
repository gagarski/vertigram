package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.entities.StickerSet

data class GetStickerSet(
    val name: String
) : JsonTgCallable<StickerSet>()
