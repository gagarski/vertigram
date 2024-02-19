package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.ReplyParameters
import ski.gagar.vertigram.types.richtext.HasRichText
import ski.gagar.vertigram.types.richtext.RichText
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [sendMessage](https://core.telegram.org/bots/api#sendmessage) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class SendMessage(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    override val text: String,
    override val parseMode: RichText.ParseMode? = null,
    override val entities: List<MessageEntity>? = null,
    val linkPreviewOptions: Message.LinkPreviewOptions? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId, HasRichText
