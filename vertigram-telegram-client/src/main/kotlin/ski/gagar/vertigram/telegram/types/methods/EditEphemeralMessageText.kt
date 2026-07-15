package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.HasReceiverUserId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.richtext.HasRichText
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [editEphemeralMessageText](https://core.telegram.org/bots/api#editephemeralmessagetext) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class EditEphemeralMessageText internal constructor(
    override val chatId: ChatId,
    override val receiverUserId: Long,
    val ephemeralMessageId: Long,
    override val text: String,
    override val parseMode: RichText.ParseMode? = null,
    override val entities: List<MessageEntity>? = null,
    val linkPreviewOptions: Message.LinkPreviewOptions? = null,
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null
) : JsonTelegramCallable<Boolean>(), HasChatId, HasReceiverUserId, HasRichText
