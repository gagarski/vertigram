package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [replaceStickerInSet](https://core.telegram.org/bots/api#replacestickerinset) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ReplaceStickerInSet(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val userId: Long,
    val name: String,
    val oldSticker: String,
    @TelegramMedia
    val sticker: InputMedia.Sticker
) : MultipartTelegramCallable<Boolean>()
