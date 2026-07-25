package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * This object contains basic information about an invoice.
 *
 * See Telegram's [Invoice](https://core.telegram.org/bots/api#invoice) documentation.
 */
@TelegramCodegen.Type
data class Invoice internal constructor(
    /** Product name. */
    val title: String,
    /** Product description. */
    val description: String,
    /** Unique bot deep-linking parameter that can be used to generate this invoice. */
    val startParameter: String,
    /** Three-letter ISO 4217 currency code, or `XTR` for payments in Telegram Stars. */
    val currency: String,
    /** Total price in the smallest units of [currency]. */
    val totalAmount: Int
) {
    companion object
}
