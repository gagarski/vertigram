package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.UserChatBoosts
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getUserChatBoosts](https://core.telegram.org/bots/api#getuserchatboosts) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetUserChatBoosts internal constructor(
    val chatId: ChatId,
    val userId: Long
) : JsonTelegramCallable<UserChatBoosts>()
