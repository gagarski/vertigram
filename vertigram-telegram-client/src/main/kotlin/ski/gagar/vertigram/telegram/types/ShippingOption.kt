package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Represents one shipping option.
 *
 * See Telegram's [ShippingOption](https://core.telegram.org/bots/api#shippingoption) documentation.
 */
@TelegramCodegen.Type
data class ShippingOption internal constructor(
    /** Shipping option identifier. */
    val id: String,
    /** Option title. */
    val title: String,
    /** List of price portions. */
    val prices: List<LabeledPrice>
) {
    companion object
}
