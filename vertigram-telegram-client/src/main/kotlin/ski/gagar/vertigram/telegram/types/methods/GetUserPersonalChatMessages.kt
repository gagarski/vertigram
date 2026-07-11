package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Message

/**
 * Telegram [getUserPersonalChatMessages](https://core.telegram.org/bots/api#getuserpersonalchatmessages) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetUserPersonalChatMessages internal constructor(
    val userId: Long,
    val limit: Int
) : JsonTelegramCallable<List<Message>>()
