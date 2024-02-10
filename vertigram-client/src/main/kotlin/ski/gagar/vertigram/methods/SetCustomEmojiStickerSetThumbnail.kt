package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.Message

data class SetCustomEmojiStickerSetThumbnail(
    val name: String,
    val customEmojiId: String? = null
) : MultipartTelegramCallable<Message>()
