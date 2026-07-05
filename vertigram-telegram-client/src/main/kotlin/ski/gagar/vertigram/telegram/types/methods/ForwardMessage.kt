package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.SuggestedPost
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration

/**
 * Telegram [forwardMessage](https://core.telegram.org/bots/api#forwardmessage) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class ForwardMessage internal constructor(
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val directMessagesTopicId: Long? = null,
    val fromChatId: ChatId,
    val videoStartTimestamp: Duration? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val suggestedPostParameters: SuggestedPost.Parameters? = null,
    val messageId: Long
) : JsonTelegramCallable<Message>(), HasChatId
