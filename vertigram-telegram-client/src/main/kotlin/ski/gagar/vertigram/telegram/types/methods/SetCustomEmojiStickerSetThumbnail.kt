package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to set the thumbnail of a custom emoji sticker set. Returns `true` on success.
 *
 * See Telegram's
 * [setCustomEmojiStickerSetThumbnail](https://core.telegram.org/bots/api#setcustomemojistickersetthumbnail)
 * documentation.
 */
@TelegramCodegen.Method
data class SetCustomEmojiStickerSetThumbnail internal constructor(
    /** Sticker set name. */
    val name: String,
    /** Custom emoji identifier of a sticker from the sticker set to use as the thumbnail. */
    val customEmojiId: String? = null
) : MultipartTelegramCallable<Message>()
