package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type OrderInfo.
 */
data class OrderInfo(
    val name: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val shippingAddress: ShippingAddress? = null
)
