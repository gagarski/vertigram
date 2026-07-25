package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to hide the 'General' topic in a forum supergroup chat.
 *
 * The bot must be an administrator in the chat for this to work and must have
 * [ski.gagar.vertigram.telegram.types.ChatAdministratorRights.canManageTopics] administrator rights. The topic will
 * be automatically closed if it was open. Returns `true` on success.
 *
 * See Telegram's [hideGeneralForumTopic](https://core.telegram.org/bots/api#hidegeneralforumtopic) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class HideGeneralForumTopic internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
