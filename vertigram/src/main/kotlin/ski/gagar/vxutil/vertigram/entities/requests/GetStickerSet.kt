package ski.gagar.vxutil.vertigram.entities.requests

import ski.gagar.vxutil.vertigram.entities.StickerSet

data class GetStickerSet(
    val name: String
) : JsonTgCallable<StickerSet>()
