package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ShippingOption.
 */
data class ShippingOption(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>
)
