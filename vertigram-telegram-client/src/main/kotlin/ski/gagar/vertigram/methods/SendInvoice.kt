package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.types.LabeledPrice
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.ReplyParameters
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [sendInvoice](https://core.telegram.org/bots/api#sendinvoice) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class SendInvoice(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val title: String,
    val description: String,
    val payload: String,
    val providerToken: String,
    val currency: String,
    val prices: List<LabeledPrice>,
    val maxTipAmount: Int? = null,
    val suggestedTipAmounts: List<Int>? = null,
    val startParameter: String? = null,
    val providerData: String? = null,
    val photoUrl: String? = null,
    val photoSize: Int? = null,
    val photoWidth: Int? = null,
    val photoHeight: Int? = null,
    val needName: Boolean = false,
    val needPhoneNumber: Boolean = false,
    val needEmail: Boolean = false,
    val needShippingAddress: Boolean = false,
    val sendPhoneNumberToProvider: Boolean = false,
    val sendEmailToProvider: Boolean = false,
    @get:JvmName("getIsFlexible")
    val isFlexible: Boolean = false,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId
