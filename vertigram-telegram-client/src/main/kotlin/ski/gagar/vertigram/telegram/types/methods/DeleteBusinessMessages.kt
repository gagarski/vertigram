package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BusinessConnection

/**
 * Delete messages on behalf of a business account. Requires the
 * [BusinessConnection.BotRights.canDeleteSentMessages] business bot right to delete messages sent by the bot itself,
 * or the [BusinessConnection.BotRights.canDeleteAllMessages] business bot right to delete any message. Returns `true`
 * on success.
 *
 * See Telegram's [deleteBusinessMessages](https://core.telegram.org/bots/api#deletebusinessmessages) documentation.
 */
@TelegramCodegen.Method
data class DeleteBusinessMessages internal constructor(
    /** Unique identifier of the business connection on behalf of which to delete the messages. */
    val businessConnectionId: String,
    /**
     * List of 1-100 identifiers of messages to delete. All messages must be from the same chat. See
     * [ski.gagar.vertigram.telegram.methods.deleteMessage] for limitations on which messages can be deleted.
     */
    val messageIds: List<Long>
) : JsonTelegramCallable<Boolean>()
