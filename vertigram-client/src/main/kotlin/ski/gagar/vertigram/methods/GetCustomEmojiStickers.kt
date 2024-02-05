package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.Sticker

@TgMethod
data class GetCustomEmojiStickers(
    val customEmojiIds: List<String>
) : JsonTgCallable<List<Sticker>>()
