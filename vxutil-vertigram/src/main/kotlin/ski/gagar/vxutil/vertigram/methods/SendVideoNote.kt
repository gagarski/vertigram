package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ReplyMarkup
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.util.multipart.TgMedia
import java.time.Duration

@TgMethod
data class SendVideoNote(
    val chatId: ChatId,
    @TgMedia
    val videoNote: Attachment,
    val duration: Duration? = null,
    val length: Int? = null,
    @TgMedia
    val thumb: Attachment? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    val replyMarkup: ReplyMarkup? = null,
    // Since Telegram Bot Api 6.3
    val messageThreadId: Long? = null
) : MultipartTgCallable<Message>()
