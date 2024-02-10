package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.Message

data class SetStickerKeywords(
    val sticker: String,
    val keywords: List<String> = listOf()
) : MultipartTelegramCallable<Message>()
