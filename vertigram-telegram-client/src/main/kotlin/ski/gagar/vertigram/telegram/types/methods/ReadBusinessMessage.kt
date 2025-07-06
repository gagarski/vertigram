package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong

/**
 * Telegram [readBusinessMessage](https://core.telegram.org/bots/api#readbusinessmessage) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class ReadBusinessMessage internal constructor(
    val businessConnectionId: String,
    override val chatId: Long,
    val messageId: Long
) : JsonTelegramCallable<Boolean>(), HasChatIdLong
