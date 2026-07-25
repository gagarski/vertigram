package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to close an open 'General' topic in a forum supergroup chat. The bot must be an administrator in the
 * chat for this to work and must have the [ChatAdministratorRights.canManageTopics] administrator rights. Returns
 * `true` on success.
 *
 * See Telegram's
 * [closeGeneralForumTopic](https://core.telegram.org/bots/api#closegeneralforumtopic) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class CloseGeneralForumTopic internal constructor(
    /** Unique identifier for the target chat or username of the target supergroup. */
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
