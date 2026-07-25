package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.HasReceiverUserId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to delete an ephemeral message. Note that it is not guaranteed that the user will receive the
 * message deletion event, especially if they are offline. Returns `true` on success.
 *
 * See Telegram's [deleteEphemeralMessage](https://core.telegram.org/bots/api#deleteephemeralmessage) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class DeleteEphemeralMessage internal constructor(
    /** Unique identifier for the target chat or username of the target supergroup. */
    override val chatId: ChatId,
    /** Identifier of the user who received the message. */
    override val receiverUserId: Long,
    /** Identifier of the ephemeral message to delete. */
    val ephemeralMessageId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId, HasReceiverUserId
