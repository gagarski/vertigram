package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type PreCheckoutQuery.
 */
data class PreCheckoutQuery(
    val id: String,
    val from: User,
    val currency: String,
    val totalAmount: Long,
    val invoicePayload: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo? = null
)
