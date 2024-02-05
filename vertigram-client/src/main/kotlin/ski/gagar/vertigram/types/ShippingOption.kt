package ski.gagar.vertigram.types

data class ShippingOption(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>
)
