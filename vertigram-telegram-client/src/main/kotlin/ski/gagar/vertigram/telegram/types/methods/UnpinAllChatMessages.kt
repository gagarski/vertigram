package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to clear the list of pinned messages in a chat. Returns `true` on success.
 *
 * See Telegram's [unpinAllChatMessages](https://core.telegram.org/bots/api#unpinallchatmessages) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class UnpinAllChatMessages internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
