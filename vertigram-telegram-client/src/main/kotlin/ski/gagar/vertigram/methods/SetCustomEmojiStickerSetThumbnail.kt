package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setCustomEmojiStickerSetThumbnail](https://core.telegram.org/bots/api#setcustomemojistickersetthumbnail) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetCustomEmojiStickerSetThumbnail(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val name: String,
    val customEmojiId: String? = null
) : MultipartTelegramCallable<Message>()
