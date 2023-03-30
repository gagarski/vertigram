package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.throttling.Throttled
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.MessageEntity
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.ReplyMarkup
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.util.multipart.TgMedia
import java.time.Duration

@TgMethod
@Throttled
data class SendVideo(
    override val chatId: ChatId,
    @TgMedia
    val video: Attachment,
    val duration: Duration? = null,
    val width: Int? = null,
    val height: Int? = null,
    @TgMedia
    val thumbnail: Attachment? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val supportsStreaming: Boolean = false,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    val replyMarkup: ReplyMarkup? = null,
    // Since Telegram Bot API 6.3
    val messageThreadId: Long? = null,
    // Since Telegram Bot API 6.4
    val hasSpoiler: Boolean = false
) : MultipartTgCallable<Message>(), HasChatId
