package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Represents information about an order.
 *
 * See Telegram's [OrderInfo](https://core.telegram.org/bots/api#orderinfo) documentation.
 */
@TelegramCodegen.Type
data class OrderInfo internal constructor(
    /** User name. */
    val name: String? = null,
    /** User's phone number. */
    val phoneNumber: String? = null,
    /** User email. */
    val email: String? = null,
    /** User shipping address. */
    val shippingAddress: ShippingAddress? = null
) {
    companion object
}
