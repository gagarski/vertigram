package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerEmojiList](https://core.telegram.org/bots/api#setstickeremojilist) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
data class SetStickerEmojiList(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val sticker: String,
    val emojiList: List<String>
) : MultipartTelegramCallable<Boolean>()
