package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [deleteMessages](https://core.telegram.org/bots/api#deletemessages) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class DeleteMessages internal constructor(
    override val chatId: ChatId,
    val messageIds: List<Long>
) : JsonTelegramCallable<Boolean>(), HasChatId
