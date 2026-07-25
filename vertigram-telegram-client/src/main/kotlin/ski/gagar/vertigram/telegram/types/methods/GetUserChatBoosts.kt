package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.UserChatBoosts
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get the list of boosts added to a chat by a user.
 *
 * Requires administrator rights in the chat. Returns a [UserChatBoosts] object.
 *
 * See Telegram's [getUserChatBoosts](https://core.telegram.org/bots/api#getuserchatboosts) documentation.
 */
@TelegramCodegen.Method
data class GetUserChatBoosts internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    val chatId: ChatId,
    /** Unique identifier of the target user. */
    val userId: Long
) : JsonTelegramCallable<UserChatBoosts>()
