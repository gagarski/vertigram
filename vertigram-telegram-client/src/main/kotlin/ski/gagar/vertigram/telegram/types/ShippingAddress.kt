package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Represents a shipping address.
 *
 * See Telegram's [ShippingAddress](https://core.telegram.org/bots/api#shippingaddress) documentation.
 */
@TelegramCodegen.Type
data class ShippingAddress internal constructor(
    /** Two-letter ISO 3166-1 alpha-2 country code. */
    val countryCode: String,
    /** State, if applicable. */
    val state: String,
    /** City. */
    val city: String,
    /** First line of the address. */
    val streetLine1: String,
    /** Second line of the address. */
    val streetLine2: String,
    /** Address post code. */
    val postCode: String
) {
    companion object
}
