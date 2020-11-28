package ski.gagar.vertigram.entities

data class OrderInfo(
    val name: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val shippingAddress: ShippingAddress? = null
)