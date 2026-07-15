package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.HasReceiverUserId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.SuggestedPost
import ski.gagar.vertigram.telegram.types.richtext.HasRichText
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [sendMessage](https://core.telegram.org/bots/api#sendmessage) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class SendMessage internal constructor(
    val businessConnectionId: String? = null,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val directMessagesTopicId: Long? = null,
    override val text: String,
    override val parseMode: RichText.ParseMode? = null,
    override val entities: List<MessageEntity>? = null,
    val linkPreviewOptions: Message.LinkPreviewOptions? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val allowPaidBroadcast: Boolean = false,
    val messageEffectId: String? = null,
    val suggestedPostParameters: SuggestedPost.Parameters? = null,
    override val receiverUserId: Long? = null,
    val callbackQueryId: String? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId, HasReceiverUserId, HasRichText
