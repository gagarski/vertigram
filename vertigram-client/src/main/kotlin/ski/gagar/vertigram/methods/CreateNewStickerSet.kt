package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramMedia
import ski.gagar.vertigram.types.InputMedia
import ski.gagar.vertigram.types.StickerFormat
import ski.gagar.vertigram.types.StickerType
import ski.gagar.vertigram.util.NoPosArgs

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
    val stickers: List<InputMedia.Sticker>,
    val stickerFormat: StickerFormat,
    val stickerType: StickerType? = null,
    val needsRepainting: Boolean = false
) : MultipartTelegramCallable<Boolean>()
