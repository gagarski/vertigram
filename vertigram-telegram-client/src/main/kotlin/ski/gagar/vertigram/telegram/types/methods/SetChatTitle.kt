package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change the title of a chat. Returns `true` on success.
 *
 * See Telegram's [setChatTitle](https://core.telegram.org/bots/api#setchattitle) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class SetChatTitle internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** New chat title, 1-128 characters. */
    val title: String
) : JsonTelegramCallable<Boolean>(), HasChatId
