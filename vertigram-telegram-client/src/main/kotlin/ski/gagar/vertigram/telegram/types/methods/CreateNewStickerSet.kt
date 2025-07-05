package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.Sticker

/**
 * Telegram [createNewStickerSet](https://core.telegram.org/bots/api#createnewstickerset) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class CreateNewStickerSet internal constructor(
    val userId: Long,
    val name: String,
    val title: String,
    @TelegramMedia
    val stickers: List<InputMedia.Sticker>,
    val stickerType: Sticker.Type? = null,
    val needsRepainting: Boolean = false
) : MultipartTelegramCallable<Boolean>()
