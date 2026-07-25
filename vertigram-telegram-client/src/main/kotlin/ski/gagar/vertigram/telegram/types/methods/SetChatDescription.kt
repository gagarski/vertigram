package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change the description of a group, a supergroup or a channel. Returns `true` on success.
 *
 * See Telegram's [setChatDescription](https://core.telegram.org/bots/api#setchatdescription) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class SetChatDescription internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** New chat description, 0-255 characters. */
    val description: String
) : JsonTelegramCallable<Boolean>(), HasChatId
