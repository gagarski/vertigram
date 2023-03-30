package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.Message

@TgMethod
data class DeleteStickerSet(
    val name: String
) : MultipartTgCallable<Message>()
