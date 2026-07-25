package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to reopen a closed 'General' topic in a forum supergroup chat.
 *
 * The bot must be an administrator in the chat for this to work and must have
 * [ski.gagar.vertigram.telegram.types.ChatAdministratorRights.canManageTopics] administrator rights. The topic will
 * be automatically unhidden if it was hidden. Returns `true` on success.
 *
 * See Telegram's [reopenGeneralForumTopic](https://core.telegram.org/bots/api#reopengeneralforumtopic) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class ReopenGeneralForumTopic internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
