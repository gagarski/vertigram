package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setChatStickerSet](https://core.telegram.org/bots/api#setchatstickerset) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetChatStickerSet internal constructor(
    override val chatId: ChatId,
    val stickerSetName: String
) : JsonTelegramCallable<Boolean>(), HasChatId
