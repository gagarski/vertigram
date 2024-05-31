package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.Sticker
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
    val stickerType: Sticker.Type? = null,
    val needsRepainting: Boolean = false
) : MultipartTelegramCallable<Boolean>()
