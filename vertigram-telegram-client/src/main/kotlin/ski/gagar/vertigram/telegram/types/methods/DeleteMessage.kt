package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [deleteMessage](https://core.telegram.org/bots/api#deletemessage) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class DeleteMessage internal constructor(
    override val chatId: ChatId,
    val messageId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
