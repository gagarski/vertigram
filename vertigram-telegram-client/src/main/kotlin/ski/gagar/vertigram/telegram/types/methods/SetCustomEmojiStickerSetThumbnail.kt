package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setCustomEmojiStickerSetThumbnail](https://core.telegram.org/bots/api#setcustomemojistickersetthumbnail) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetCustomEmojiStickerSetThumbnail internal constructor(
    val name: String,
    val customEmojiId: String? = null
) : MultipartTelegramCallable<Message>()
