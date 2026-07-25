package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to remove a reaction from a message in a group or a supergroup chat. The bot must have the
 * [ChatAdministratorRights.canDeleteMessages] administrator right in the chat. Returns `true` on success.
 *
 * See Telegram's [deleteMessageReaction](https://core.telegram.org/bots/api#deletemessagereaction) documentation.
 */
@TelegramCodegen.Method
data class DeleteMessageReaction internal constructor(
    /** Unique identifier for the target chat or username of the target supergroup. */
    override val chatId: ChatId,
    /** Identifier of the target message. */
    val messageId: Long,
    /** Identifier of the user whose reaction will be removed, if the reaction was added by a user. */
    val userId: Long? = null,
    /** Identifier of the chat whose reaction will be removed, if the reaction was added by a chat. */
    val actorChatId: Long? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
