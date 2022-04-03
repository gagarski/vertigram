package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type SuccessfulPayment.
 */
data class SuccessfulPayment(
    val currency: String,
    val totalAmount: Long,
    val invoicePayload: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo? = null,
    val telegramPaymentChargeId: String,
    val providerPaymentChargeId: String
)
