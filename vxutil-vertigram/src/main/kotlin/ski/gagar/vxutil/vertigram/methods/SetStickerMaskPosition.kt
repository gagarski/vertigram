package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.MaskPosition
import ski.gagar.vxutil.vertigram.types.Message

@TgMethod
data class SetStickerMaskPosition(
    val sticker: String,
    val maskPosition: MaskPosition? = null
) : MultipartTgCallable<Message>()
