package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalRichCaption
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [sendVideo](https://core.telegram.org/bots/api#sendvideo) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen
data class SendVideo(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val businessConnectionId: String? = null,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    @TelegramMedia
    val video: Attachment,
    val duration: Duration? = null,
    val width: Int? = null,
    val height: Int? = null,
    @TelegramMedia
    val thumbnail: Attachment? = null,
    override val caption: String? = null,
    override val parseMode: RichText.ParseMode? = null,
    override val captionEntities: List<MessageEntity>? = null,
    val showCaptionAboveMedia: Boolean = false,
    val hasSpoiler: Boolean = false,
    val supportsStreaming: Boolean = false,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val messageEffectId: String? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTelegramCallable<Message>(), HasChatId, HasOptionalRichCaption
