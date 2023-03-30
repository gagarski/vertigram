package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.Message

@TgMethod
data class SetStickerKeywords(
    val sticker: String,
    val keywords: List<String> = listOf()
) : MultipartTgCallable<Message>()
