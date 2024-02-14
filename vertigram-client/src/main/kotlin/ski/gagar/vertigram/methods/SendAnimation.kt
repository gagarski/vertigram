package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramMedia
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.ParseMode
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.ReplyParameters
import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [sendAnimation](https://core.telegram.org/bots/api#sendanimation) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class SendAnimation(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    @TelegramMedia
    val animation: Attachment,
    val duration: Duration? = null,
    val width: Int? = null,
    val height: Int? = null,
    @TelegramMedia
    val thumbnail: Attachment? = null,
    @PublishedApi internal val caption: String? = null,
    @PublishedApi internal val parseMode: ParseMode? = null,
    @PublishedApi internal val captionEntities: List<MessageEntity>? = null,
    val hasSpoiler: Boolean = false,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTelegramCallable<Message>(), HasChatId
