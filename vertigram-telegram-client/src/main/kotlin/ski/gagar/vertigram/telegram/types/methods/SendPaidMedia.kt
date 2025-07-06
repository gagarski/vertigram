package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.*
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalRichCaption
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [sendPaidMedia](https://core.telegram.org/bots/api#sendpaidmedia) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class SendPaidMedia internal constructor(
    val businessConnectionId: String? = null,
    override val chatId: ChatId,
    val starCount: Int,
    val media: List<InputMedia.Paid>,
    val payload: String? = null,
    override val caption: String? = null,
    override val parseMode: RichText.ParseMode? = null,
    override val captionEntities: List<MessageEntity>? = null,
    val showCaptionAboveMedia: Boolean = false,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val allowPaidBroadcast: Boolean = false,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : MultipartTelegramCallable<Message>(), HasChatId, HasOptionalRichCaption
