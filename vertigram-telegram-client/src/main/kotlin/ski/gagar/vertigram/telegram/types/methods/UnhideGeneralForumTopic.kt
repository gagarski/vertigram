package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to unhide the 'General' topic in a forum supergroup chat. Returns `true` on success.
 *
 * See Telegram's
 * [unhideGeneralForumTopic](https://core.telegram.org/bots/api#unhidegeneralforumtopic) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class UnhideGeneralForumTopic internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
