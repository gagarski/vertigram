package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.StickerSet

data class GetStickerSet(
    val name: String
) : JsonTgCallable<StickerSet>()
