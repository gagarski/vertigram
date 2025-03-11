package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [addStickerToSet](https://core.telegram.org/bots/api#addstickertoset) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
data class AddStickerToSet(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val userId: Long,
    val name: String,
    @TelegramMedia
    val sticker: InputMedia.Sticker
) : MultipartTelegramCallable<Boolean>()
