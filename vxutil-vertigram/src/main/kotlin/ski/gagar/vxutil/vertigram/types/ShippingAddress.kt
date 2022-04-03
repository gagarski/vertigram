package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ShippingAddress.
 */
data class ShippingAddress(
    val countryCode: String,
    val state: String,
    val city: String,
    val streetLine1: String,
    val streetLine2: String,
    val postCode: String
)
