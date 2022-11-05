package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.InputMedia
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.util.multipart.TgMedia

@TgMethod
data class SendMediaGroup(
    val chatId: ChatId,
    @TgMedia
    val media: List<InputMedia>,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    // Since Telegram Bot Api 6.3
    val messageThreadId: Long? = null,
) : MultipartTgCallable<List<Message>>()
