package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [ShippingOption](https://core.telegram.org/bots/api#shippingoption) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class ShippingOption internal constructor(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>
) {
    companion object
}
