package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.InputSticker
import ski.gagar.vertigram.util.NoPosArgs
import ski.gagar.vertigram.util.multipart.TgMedia

/**
 * Telegram [addStickerToSet](https://core.telegram.org/bots/api#addstickertoset) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TgMethod
data class AddStickerToSet(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val userId: Long,
    val name: String,
    @TgMedia
    val sticker: InputSticker
) : MultipartTgCallable<Boolean>()
