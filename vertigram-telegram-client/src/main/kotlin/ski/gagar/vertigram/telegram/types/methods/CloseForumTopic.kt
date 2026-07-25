package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to close an open topic in a forum supergroup chat. The bot must be an administrator in the chat for
 * this to work and must have the [ChatAdministratorRights.canManageTopics] administrator rights, unless it is the
 * creator of the topic. Returns `true` on success.
 *
 * See Telegram's [closeForumTopic](https://core.telegram.org/bots/api#closeforumtopic) documentation.
 */
@TelegramCodegen.Method
data class CloseForumTopic internal constructor(
    /** Unique identifier for the target chat or username of the target supergroup. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread of the forum topic. */
    val messageThreadId: Long,
) : JsonTelegramCallable<Boolean>(), HasChatId
