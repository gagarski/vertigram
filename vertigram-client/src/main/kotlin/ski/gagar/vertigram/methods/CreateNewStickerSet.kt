package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.InputSticker
import ski.gagar.vertigram.types.StickerFormat
import ski.gagar.vertigram.types.StickerType
import ski.gagar.vertigram.util.NoPosArgs
import ski.gagar.vertigram.annotations.TelegramMedia

/**
 * Telegram [createNewStickerSet](https://core.telegram.org/bots/api#createnewstickerset) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class CreateNewStickerSet(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val userId: Long,
    val name: String,
    val title: String,
    @TelegramMedia
    val stickers: List<InputSticker>,
    val stickerFormat: StickerFormat,
    val stickerType: StickerType = Defaults.stickerType,
    val needsRepainting: Boolean = false
) : MultipartTelegramCallable<Boolean>() {
    object Defaults {
        val stickerType = StickerType.REGULAR
    }
}
