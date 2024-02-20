package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [ShippingQuery](https://core.telegram.org/bots/api#shippingquery) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ShippingQuery(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val id: String,
    val from: User,
    val invoicePayload: String,
    val shippingAddress: ShippingAddress
)
