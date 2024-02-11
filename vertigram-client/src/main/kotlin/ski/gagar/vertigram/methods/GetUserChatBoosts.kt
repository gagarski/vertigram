package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.UserChatBoosts
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getUserChatBoosts](https://core.telegram.org/bots/api#getuserchatboosts) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class GetUserChatBoosts(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val chatId: ChatId,
    val userId: Long
) : JsonTelegramCallable<UserChatBoosts>()
