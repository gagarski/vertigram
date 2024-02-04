package ski.gagar.vertigram.types

import ski.gagar.vertigram.types.attachments.Attachment

data class InputSticker(
    val sticker: Attachment,
    val emojiList: List<String>,
    val maskPosition: MaskPosition? = null,
    val keywords: List<String>? = null
) {
    fun instantiate(sticker: Attachment) = copy(sticker = sticker)
}
