package ski.gagar.vertigram.types

data class SuccessfulPayment(
    val currency: String,
    val totalAmount: Int,
    val invoicePayload: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo? = null,
    val telegramPaymentChargeId: String,
    val providerPaymentChargeId: String
)
