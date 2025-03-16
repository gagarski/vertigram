package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [StickerSet](https://core.telegram.org/bots/api#stickerset) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class StickerSet internal constructor(
    val name: String,
    val title: String,
    val stickerType: Sticker.Type,
    val stickers: List<Sticker> = listOf(),
    val thumbnail: List<PhotoSize>? = null
) {
    companion object
}
