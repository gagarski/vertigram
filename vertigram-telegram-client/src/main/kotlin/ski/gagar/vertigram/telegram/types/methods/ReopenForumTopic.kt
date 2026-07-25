package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to reopen a closed topic in a forum supergroup chat.
 *
 * The bot must be an administrator in the chat for this to work and must have
 * [ski.gagar.vertigram.telegram.types.ChatAdministratorRights.canManageTopics] administrator rights, unless it is
 * the creator of the topic. Returns `true` on success.
 *
 * See Telegram's [reopenForumTopic](https://core.telegram.org/bots/api#reopenforumtopic) documentation.
 */
@TelegramCodegen.Method
data class ReopenForumTopic internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread of the forum topic. */
    val messageThreadId: Long,
) : JsonTelegramCallable<Boolean>(), HasChatId
