package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong

/**
 * Marks an incoming message as read on behalf of a business account. Requires the
 * [ski.gagar.vertigram.telegram.types.BusinessConnection.BotRights.canReadMessages] business bot right.
 * Returns `true` on success.
 *
 * See Telegram's [readBusinessMessage](https://core.telegram.org/bots/api#readbusinessmessage) documentation.
 */
@TelegramCodegen.Method()
data class ReadBusinessMessage internal constructor(
    /** Unique identifier of the business connection on behalf of which to read the message. */
    val businessConnectionId: String,
    /** Unique identifier of the chat in which the message was received. */
    override val chatId: Long,
    /** Unique identifier of the message to mark as read. */
    val messageId: Long
) : JsonTelegramCallable<Boolean>(), HasChatIdLong
