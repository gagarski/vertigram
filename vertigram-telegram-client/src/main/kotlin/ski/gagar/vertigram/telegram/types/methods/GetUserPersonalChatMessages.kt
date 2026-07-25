package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Message

/**
 * Returns the most recent messages from the personal chat of a user.
 *
 * See Telegram's
 * [getUserPersonalChatMessages](https://core.telegram.org/bots/api#getuserpersonalchatmessages) documentation.
 */
@TelegramCodegen.Method
data class GetUserPersonalChatMessages internal constructor(
    /** Unique identifier of the user. */
    val userId: Long,
    /** Number of messages to retrieve; 1-100. */
    val limit: Int
) : JsonTelegramCallable<List<Message>>()
