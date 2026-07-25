package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputMedia

/**
 * Use this method to add a new sticker to a set created by the bot.
 *
 * Emoji sticker sets can have up to 200 stickers. Other sticker sets can have up to 120 stickers.
 * Returns `true` on success.
 *
 * See the official Telegram Bot API documentation for
 * [addStickerToSet](https://core.telegram.org/bots/api#addstickertoset).
 */
@TelegramCodegen.Method
data class AddStickerToSet internal constructor(
    /** User identifier of sticker set owner. */
    val userId: Long,
    /** Sticker set name. */
    val name: String,
    /**
     * Information about the added sticker. Telegram's `InputSticker` is represented by [InputMedia.Sticker].
     * If exactly the same sticker had already been added to the set, then the set isn't changed.
     */
    val sticker: InputMedia.Sticker
) : MultipartTelegramCallable<Boolean>()
