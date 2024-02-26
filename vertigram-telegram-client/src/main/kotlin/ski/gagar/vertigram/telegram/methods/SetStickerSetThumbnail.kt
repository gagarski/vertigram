package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerSetThumbnail](https://core.telegram.org/bots/api#setstickersetthumbnail) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetStickerSetThumbnail(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val name: String,
    val userId: Long,
    @ski.gagar.vertigram.telegram.annotations.TelegramMedia
    val thumbnail: Attachment? = null
) : MultipartTelegramCallable<Boolean>()
