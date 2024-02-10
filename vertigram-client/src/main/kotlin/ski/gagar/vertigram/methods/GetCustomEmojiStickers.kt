package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.Sticker

data class GetCustomEmojiStickers(
    val customEmojiIds: List<String>
) : JsonTelegramCallable<List<Sticker>>()
