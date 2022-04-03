package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ShippingQuery.
 */
data class ShippingQuery(
    val id: String,
    val from: User,
    val invoicePayload: String,
    val shippingAddress: ShippingAddress
)
