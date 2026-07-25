package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.Sticker

/**
 * Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus
 * created. Returns `true` on success.
 *
 * See Telegram's [createNewStickerSet](https://core.telegram.org/bots/api#createnewstickerset) documentation.
 */
@TelegramCodegen.Method
data class CreateNewStickerSet internal constructor(
    /** User identifier of the created sticker set owner. */
    val userId: Long,
    /**
     * Short name of the sticker set, to be used in `t.me/addstickers/` URLs. Can contain only English letters, digits,
     * and underscores. Must begin with a letter, can't contain consecutive underscores, and must end in
     * `"_by_<bot_username>"`. `<bot_username>` is case insensitive. The name must contain 1-64 characters.
     */
    val name: String,
    /** Sticker set title, 1-64 characters. */
    val title: String,
    /**
     * List of 1-50 initial stickers to be added to the sticker set. Telegram's `InputSticker` is represented by
     * [InputMedia.Sticker].
     */
    val stickers: List<InputMedia.Sticker>,
    /** Type of stickers in the set. */
    val stickerType: Sticker.Type? = null,
    /**
     * Pass `true` if stickers in the set must be repainted to the color of text when used in messages, the accent
     * color if used as emoji status, white on chat photos, or another appropriate color based on context; for custom
     * emoji sticker sets only.
     */
    val needsRepainting: Boolean = false
) : MultipartTelegramCallable<Boolean>()
