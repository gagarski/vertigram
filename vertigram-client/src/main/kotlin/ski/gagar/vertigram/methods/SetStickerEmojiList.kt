package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.Message

data class SetStickerEmojiList(
    val sticker: String,
    val emojiList: List<String>
) : MultipartTelegramCallable<Message>()
