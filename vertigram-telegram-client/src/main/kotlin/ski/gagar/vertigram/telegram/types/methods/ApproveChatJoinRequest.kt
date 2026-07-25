package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to approve a chat join request. The bot must be an administrator in the chat for this to work and
 * must have the `can_invite_users` administrator right. Returns `true` on success.
 *
 * See Telegram's [approveChatJoinRequest](https://core.telegram.org/bots/api#approvechatjoinrequest) documentation.
 */
@TelegramCodegen.Method
data class ApproveChatJoinRequest internal constructor(
    /** Unique identifier for the target chat or username of the target channel. */
    override val chatId: ChatId,
    /** Unique identifier of the target user. */
    val userId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
