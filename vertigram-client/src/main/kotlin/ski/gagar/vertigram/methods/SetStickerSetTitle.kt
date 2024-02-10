package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.Message

data class SetStickerSetTitle(
    val name: String,
    val title: String
) : MultipartTelegramCallable<Message>()
