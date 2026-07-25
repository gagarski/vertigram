package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to delete a chat photo. Photos can't be changed for private chats. The bot must be an administrator
 * in the chat for this to work and must have the appropriate administrator rights. Returns `true` on success.
 *
 * See Telegram's [deleteChatPhoto](https://core.telegram.org/bots/api#deletechatphoto) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class DeleteChatPhoto internal constructor(
    /** Unique identifier for the target chat or username of the target channel. */
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
