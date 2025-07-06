package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [StickerSet](https://core.telegram.org/bots/api#stickerset) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type(wrapRichText = false)
data class StickerSet internal constructor(
    val name: String,
    val title: String,
    val stickerType: Sticker.Type,
    val stickers: List<Sticker> = listOf(),
    val thumbnail: List<PhotoSize>? = null
) {
    companion object
}
