package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.telegram.types.attachments.Attachment

/**
 * Use this method to set the thumbnail of a regular or mask sticker set. Returns `true` on success.
 *
 * See Telegram's [setStickerSetThumbnail](https://core.telegram.org/bots/api#setstickersetthumbnail) documentation.
 */
@TelegramCodegen.Method
data class SetStickerSetThumbnail internal constructor(
    /** Sticker set name. */
    val name: String,
    /** User identifier of the sticker set owner. */
    val userId: Long,
    /** Sticker set thumbnail. */
    val thumbnail: Attachment? = null,
    /** Format of the thumbnail. */
    val format: Sticker.Format
) : MultipartTelegramCallable<Boolean>()
