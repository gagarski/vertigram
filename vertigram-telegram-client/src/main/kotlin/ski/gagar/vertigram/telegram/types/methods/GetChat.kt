package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.Chat
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get up-to-date information about the chat.
 *
 * See Telegram's [getChat](https://core.telegram.org/bots/api#getchat) documentation.
 */
@TelegramCodegen.Method
data class GetChat internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId
) : JsonTelegramCallable<Chat.FullInfo>(), HasChatId
