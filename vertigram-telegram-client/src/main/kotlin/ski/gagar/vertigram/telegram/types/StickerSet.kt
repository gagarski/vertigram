package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Represents a sticker set.
 *
 * See Telegram's [StickerSet](https://core.telegram.org/bots/api#stickerset) documentation.
 */
@TelegramCodegen.Type
data class StickerSet internal constructor(
    /** Sticker set name. */
    val name: String,
    /** Sticker set title. */
    val title: String,
    /** Type of stickers in the set. */
    val stickerType: Sticker.Type,
    /** Stickers in the set. */
    val stickers: List<Sticker> = listOf(),
    /** Sticker set thumbnail. */
    val thumbnail: List<PhotoSize>? = null
) {
    companion object
}
