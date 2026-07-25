package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.LabeledPrice
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.REDACTED_SENSITIVE_DATA
import ski.gagar.vertigram.telegram.types.SensitiveData
import ski.gagar.vertigram.telegram.types.SuggestedPost
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to send invoices.
 *
 * See Telegram's [sendInvoice](https://core.telegram.org/bots/api#sendinvoice) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class SendInvoice internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Identifier of the direct messages topic to which the message will be sent. */
    val directMessagesTopicId: Long? = null,
    /** Track or product title. */
    val title: String,
    /** Product description. */
    val description: String,
    /** Bot-defined payload. */
    val payload: String,
    /** Payment provider token. */
    val providerToken: String? = null,
    /** Three-letter ISO 4217 currency code. */
    val currency: String,
    /** Price portions for the product. */
    val prices: List<LabeledPrice>,
    /** Maximum accepted tip amount in the smallest units of the currency. */
    val maxTipAmount: Int? = null,
    /** Suggested tip amounts in the smallest units of the currency. */
    val suggestedTipAmounts: List<Int>? = null,
    /** Deep-linking parameter for the invoice. */
    val startParameter: String? = null,
    /** Data about the invoice required by the payment provider. */
    val providerData: String? = null,
    /** URL of the product photo. */
    val photoUrl: String? = null,
    /** Product photo size in bytes. */
    val photoSize: Int? = null,
    /** Product photo width. */
    val photoWidth: Int? = null,
    /** Product photo height. */
    val photoHeight: Int? = null,
    /** Pass `true` to require the user's full name. */
    val needName: Boolean = false,
    /** Pass `true` to require the user's phone number. */
    val needPhoneNumber: Boolean = false,
    /** Pass `true` to require the user's email address. */
    val needEmail: Boolean = false,
    /** Pass `true` to require the user's shipping address. */
    val needShippingAddress: Boolean = false,
    /** Pass `true` to send the user's phone number to the provider. */
    val sendPhoneNumberToProvider: Boolean = false,
    /** Pass `true` to send the user's email address to the provider. */
    val sendEmailToProvider: Boolean = false,
    @get:JvmName("getIsFlexible")
    /** Pass `true` if the final price depends on the shipping method. */
    val isFlexible: Boolean = false,
    /** Sends the message silently. */
    val disableNotification: Boolean = false,
    /** Protects the sent message from forwarding and saving. */
    val protectContent: Boolean = false,
    /** Pass `true` to allow up to 1000 messages per second for a fee in Telegram Stars. */
    val allowPaidBroadcast: Boolean = false,
    /** Unique identifier of the message effect added to the message. */
    val messageEffectId: String? = null,
    /** Parameters of the suggested post to send. */
    val suggestedPostParameters: SuggestedPost.Parameters? = null,
    /** Parameters of the message being replied to. */
    val replyParameters: ReplyParameters? = null,
    /** Additional interface options. */
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId, SensitiveData<SendInvoice> {
    override fun copyWithoutSensitiveData() =
        copy(providerToken = providerToken?.let { REDACTED_SENSITIVE_DATA })
}
