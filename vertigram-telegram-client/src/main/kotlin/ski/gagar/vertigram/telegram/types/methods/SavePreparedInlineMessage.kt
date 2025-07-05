package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InlineQuery
import ski.gagar.vertigram.telegram.types.PreparedInlineMessage
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerEmojiList](https://core.telegram.org/bots/api#setstickeremojilist) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SavePreparedInlineMessage internal constructor(
    val userId: Long,
    val result: InlineQuery.Result,
    val allowUserChats: Boolean = false,
    val allowBotChats: Boolean = false,
    val allowGroupChats: Boolean = false,
    val allowChannelChats: Boolean = false
) : JsonTelegramCallable<PreparedInlineMessage>()
