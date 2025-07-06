package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration

/**
 * Telegram [sendVideoNote](https://core.telegram.org/bots/api#sendvideonote) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class SendVideoNote internal constructor(
    val businessConnectionId: String? = null,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val videoNote: Attachment,
    val duration: Duration? = null,
    val length: Int? = null,
    val thumbnail: Attachment? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val allowPaidBroadcast: Boolean = false,
    val messageEffectId: String? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTelegramCallable<Message>(), HasChatId
