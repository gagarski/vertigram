package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to delete a forum topic along with all its messages in a forum supergroup chat or a private chat
 * with a user. In the case of a supergroup chat, the bot must be an administrator in the chat for this to work and
 * must have the [ChatAdministratorRights.canDeleteMessages] administrator rights. Returns `true` on success.
 *
 * See Telegram's [deleteForumTopic](https://core.telegram.org/bots/api#deleteforumtopic) documentation.
 */
@TelegramCodegen.Method
data class DeleteForumTopic internal constructor(
    /** Unique identifier for the target chat or username of the target supergroup. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread of the forum topic. */
    val messageThreadId: Long,
) : JsonTelegramCallable<Boolean>(), HasChatId
