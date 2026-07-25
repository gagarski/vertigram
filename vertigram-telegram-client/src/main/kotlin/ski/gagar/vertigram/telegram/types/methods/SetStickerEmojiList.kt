package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change the list of emoji assigned to a regular or custom emoji sticker.
 *
 * Returns `true` on success.
 *
 * See Telegram's [setStickerEmojiList](https://core.telegram.org/bots/api#setstickeremojilist) documentation.
 */
@TelegramCodegen.Method
data class SetStickerEmojiList internal constructor(
    /** File identifier of the sticker. */
    val sticker: String,
    /** One to 20 emoji associated with the sticker. */
    val emojiList: List<String>
) : MultipartTelegramCallable<Boolean>()
