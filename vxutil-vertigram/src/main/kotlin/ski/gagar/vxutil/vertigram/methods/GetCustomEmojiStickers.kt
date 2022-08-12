package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.Sticker

@TgMethod
data class GetCustomEmojiStickers(
    val customEmojiIds: List<String>
) : JsonTgCallable<List<Sticker>>()
