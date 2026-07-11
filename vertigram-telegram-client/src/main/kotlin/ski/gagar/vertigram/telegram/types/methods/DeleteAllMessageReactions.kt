package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [deleteAllMessageReactions](https://core.telegram.org/bots/api#deleteallmessagereactions) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class DeleteAllMessageReactions internal constructor(
    override val chatId: ChatId,
    val userId: Long? = null,
    val actorChatId: Long? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
