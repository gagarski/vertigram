package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [replaceStickerInSet](https://core.telegram.org/bots/api#replacestickerinset) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class ReplaceStickerInSet internal constructor(
    val userId: Long,
    val name: String,
    val oldSticker: String,
    @TelegramMedia
    val sticker: InputMedia.Sticker
) : MultipartTelegramCallable<Boolean>()
