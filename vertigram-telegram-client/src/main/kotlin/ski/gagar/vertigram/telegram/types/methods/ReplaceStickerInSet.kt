package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputMedia

/**
 * Use this method to replace an existing sticker in a sticker set with a new one.
 *
 * The method is equivalent to calling
 * [ski.gagar.vertigram.telegram.client.Telegram.deleteStickerFromSet], then
 * [ski.gagar.vertigram.telegram.client.Telegram.addStickerToSet], then
 * [ski.gagar.vertigram.telegram.client.Telegram.setStickerPositionInSet]. Returns `true` on success.
 *
 * See Telegram's [replaceStickerInSet](https://core.telegram.org/bots/api#replacestickerinset) documentation.
 */
@TelegramCodegen.Method
data class ReplaceStickerInSet internal constructor(
    /** User identifier of the sticker set owner. */
    val userId: Long,
    /** Sticker set name. */
    val name: String,
    /** File identifier of the replaced sticker. */
    val oldSticker: String,
    /** Sticker to add to the set. */
    val sticker: InputMedia.Sticker
) : MultipartTelegramCallable<Boolean>()
