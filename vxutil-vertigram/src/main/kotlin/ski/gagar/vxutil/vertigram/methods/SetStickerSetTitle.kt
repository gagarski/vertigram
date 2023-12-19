package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.Message

@TgMethod
data class SetStickerSetTitle(
    val name: String,
    val title: String
) : MultipartTgCallable<Message>()