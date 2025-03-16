package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [ShippingOption](https://core.telegram.org/bots/api#shippingoption) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class ShippingOption internal constructor(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>
) {
    companion object
}
