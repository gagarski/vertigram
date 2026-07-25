package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get the number of members in a chat.
 *
 * See Telegram's [getChatMemberCount](https://core.telegram.org/bots/api#getchatmembercount) documentation.
 */
@TelegramCodegen.Method
data class GetChatMemberCount internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId
) : JsonTelegramCallable<Int>(), HasChatId
