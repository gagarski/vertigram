package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerSetThumbnail](https://core.telegram.org/bots/api#setstickersetthumbnail) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetStickerSetThumbnail internal constructor(
    val name: String,
    val userId: Long,
    @TelegramMedia
    val thumbnail: Attachment? = null,
    val format: Sticker.Format
) : MultipartTelegramCallable<Boolean>()
