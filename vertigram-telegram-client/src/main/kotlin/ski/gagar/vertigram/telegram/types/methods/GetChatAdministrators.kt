package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.ChatMember
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get a list of administrators in a chat, which aren't bots by default.
 *
 * See Telegram's [getChatAdministrators](https://core.telegram.org/bots/api#getchatadministrators) documentation.
 */
@TelegramCodegen.Method
data class GetChatAdministrators internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Pass `true` to include bots in the returned list. */
    val returnBots: Boolean = false
) : JsonTelegramCallable<List<ChatMember>>(), HasChatId
