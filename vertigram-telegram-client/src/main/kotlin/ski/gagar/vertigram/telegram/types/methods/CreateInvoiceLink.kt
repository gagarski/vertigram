package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.LabeledPrice
import ski.gagar.vertigram.telegram.types.REDACTED_SENSITIVE_DATA
import ski.gagar.vertigram.telegram.types.SensitiveData
import java.time.Duration

/**
 * Use this method to create a link for an invoice. Returns the created invoice link as a [String] on success.
 *
 * See Telegram's [createInvoiceLink](https://core.telegram.org/bots/api#createinvoicelink) documentation.
 */
@TelegramCodegen.Method
data class CreateInvoiceLink internal constructor(
    /**
     * Unique identifier of the business connection on behalf of which the link will be created. For payments in
     * [Telegram Stars](https://t.me/BotNews/90) only.
     */
    val businessConnectionId: String? = null,
    /** Product name, 1-32 characters. */
    val title: String,
    /** Product description, 1-255 characters. */
    val description: String,
    /**
     * Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user; use it for internal processes.
     */
    val payload: String,
    /**
     * Payment provider token obtained via [@BotFather](https://t.me/BotFather). Pass an empty string for payments in
     * [Telegram Stars](https://t.me/BotNews/90).
     */
    val providerToken: String? = null,
    /**
     * Three-letter ISO 4217 currency code; see [more on currencies](https://core.telegram.org/bots/payments). Pass
     * `XTR` for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    val currency: String,
    /**
     * Price breakdown containing components such as product price, tax, discount, delivery cost, delivery tax, and
     * bonus. Must contain exactly one item for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    val prices: List<LabeledPrice>,
    /**
     * The period for which the subscription will be active before the next payment. [currency] must be `XTR` if this
     * parameter is used. Telegram currently requires a period of 30 days. Any number of subscriptions can be active
     * for a given bot at the same time, including multiple concurrent subscriptions from the same user. The
     * subscription price must not exceed 10000 Telegram Stars.
     */
    val subscriptionPeriod: Duration? = null,
    /**
     * The maximum accepted amount for tips in the smallest units of [currency]. For example, for a maximum tip of
     * `US$ 1.45`, pass `145`. See the `exp` parameter in
     * [currencies.json](https://core.telegram.org/bots/payments/currencies.json) for the number of digits past the
     * decimal point. Not supported for payments in [Telegram Stars](https://t.me/BotNews/90).
     */
    val maxTipAmount: Int? = null,
    /**
     * Suggested tip amounts in the smallest units of [currency]. At most 4 amounts can be specified. They must be
     * positive, specified in a strictly increasing order, and must not exceed [maxTipAmount].
     */
    val suggestedTipAmounts: List<Int>? = null,
    /**
     * Data about the invoice that will be shared with the payment provider. The payment provider should provide a
     * detailed description of the required fields.
     */
    val providerData: String? = null,
    /** URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service. */
    val photoUrl: String? = null,
    /** Photo size in bytes. */
    val photoSize: Long? = null,
    /** Photo width. */
    val photoWidth: Int? = null,
    /** Photo height. */
    val photoHeight: Int? = null,
    /**
     * Pass `true` if you require the user's full name to complete the order. Ignored for payments in
     * [Telegram Stars](https://t.me/BotNews/90).
     */
    val needName: Boolean = false,
    /**
     * Pass `true` if you require the user's phone number to complete the order. Ignored for payments in
     * [Telegram Stars](https://t.me/BotNews/90).
     */
    val needPhoneNumber: Boolean = false,
    /**
     * Pass `true` if you require the user's email address to complete the order. Ignored for payments in
     * [Telegram Stars](https://t.me/BotNews/90).
     */
    val needEmail: Boolean = false,
    /**
     * Pass `true` if you require the user's shipping address to complete the order. Ignored for payments in
     * [Telegram Stars](https://t.me/BotNews/90).
     */
    val needShippingAddress: Boolean = false,
    /**
     * Pass `true` if the user's phone number should be sent to the provider. Ignored for payments in
     * [Telegram Stars](https://t.me/BotNews/90).
     */
    val sendPhoneNumberToProvider: Boolean = false,
    /**
     * Pass `true` if the user's email address should be sent to the provider. Ignored for payments in
     * [Telegram Stars](https://t.me/BotNews/90).
     */
    val sendEmailToProvider: Boolean = false,
    /**
     * Pass `true` if the final price depends on the shipping method. Ignored for payments in
     * [Telegram Stars](https://t.me/BotNews/90).
     */
    @get:JvmName("getIsFlexible")
    val isFlexible: Boolean
) : JsonTelegramCallable<String>(), SensitiveData<CreateInvoiceLink> {
    override fun copyWithoutSensitiveData() =
        copy(providerToken = providerToken?.let { REDACTED_SENSITIVE_DATA })
}
