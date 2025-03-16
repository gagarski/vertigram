package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [copyMessages](https://core.telegram.org/bots/api#copymessages) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class CopyMessages internal constructor(
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val fromChatId: ChatId,
    val messageIds: List<Long>,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val removeCaption: Boolean = false
) : JsonTelegramCallable<List<Message.Id>>(), HasChatId
