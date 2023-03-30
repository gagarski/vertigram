package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.Message

@TgMethod
data class SetCustomEmojiStickerSetThumbnail(
    val name: String,
    val customEmojiId: String? = null
) : MultipartTgCallable<Message>()
