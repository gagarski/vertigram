package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [ShippingOption](https://core.telegram.org/bots/api#shippingoption) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ShippingOption(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>
)
