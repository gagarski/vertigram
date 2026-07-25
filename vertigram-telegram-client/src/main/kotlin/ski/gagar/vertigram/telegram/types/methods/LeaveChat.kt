package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method for your bot to leave a group, supergroup or channel. Returns `true` on success.
 *
 * See Telegram's [leaveChat](https://core.telegram.org/bots/api#leavechat) documentation.
 */
@TelegramCodegen.Method
data class LeaveChat internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
