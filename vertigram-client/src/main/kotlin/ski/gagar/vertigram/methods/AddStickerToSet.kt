package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.InputSticker
import ski.gagar.vertigram.util.NoPosArgs
import ski.gagar.vertigram.annotations.TelegramMedia

/**
 * Telegram [addStickerToSet](https://core.telegram.org/bots/api#addstickertoset) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class AddStickerToSet(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val userId: Long,
    val name: String,
    @TelegramMedia
    val sticker: InputSticker
) : MultipartTelegramCallable<Boolean>()
