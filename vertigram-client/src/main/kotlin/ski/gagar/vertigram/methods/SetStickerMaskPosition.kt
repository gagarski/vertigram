package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.MaskPosition
import ski.gagar.vertigram.types.Message

data class SetStickerMaskPosition(
    val sticker: String,
    val maskPosition: MaskPosition? = null
) : MultipartTelegramCallable<Message>()
