package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong

/**
 * Telegram [deleteBusinessMessage](https://core.telegram.org/bots/api#deletebusinessmessage) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class DeleteBusinessMessage internal constructor(
    val businessConnectionId: String,
    override val chatId: Long,
    val messageId: Long
) : JsonTelegramCallable<Boolean>(), HasChatIdLong
