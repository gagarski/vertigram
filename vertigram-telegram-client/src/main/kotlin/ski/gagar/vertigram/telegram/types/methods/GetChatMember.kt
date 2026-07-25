package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.ChatMember
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get information about a member of a chat.
 *
 * The method is only guaranteed to work for other users if the bot is an administrator in the chat.
 *
 * See Telegram's [getChatMember](https://core.telegram.org/bots/api#getchatmember) documentation.
 */
@TelegramCodegen.Method
data class GetChatMember internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the target user. */
    val userId: Long
) : JsonTelegramCallable<ChatMember>(), HasChatId
