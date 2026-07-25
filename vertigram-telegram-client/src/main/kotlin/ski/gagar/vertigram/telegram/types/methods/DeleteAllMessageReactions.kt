package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to remove up to 10000 recent reactions in a group or a supergroup chat added by a given user or
 * chat. The bot must have the [ChatAdministratorRights.canDeleteMessages] administrator right in the chat. Returns
 * `true` on success.
 *
 * See Telegram's
 * [deleteAllMessageReactions](https://core.telegram.org/bots/api#deleteallmessagereactions) documentation.
 */
@TelegramCodegen.Method
data class DeleteAllMessageReactions internal constructor(
    /** Unique identifier for the target chat or username of the target supergroup. */
    override val chatId: ChatId,
    /** Identifier of the user whose reactions will be removed, if the reactions were added by a user. */
    val userId: Long? = null,
    /** Identifier of the chat whose reactions will be removed, if the reactions were added by a chat. */
    val actorChatId: Long? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
