package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * This object represents a portion of the price for goods or services.
 *
 * See Telegram's [LabeledPrice](https://core.telegram.org/bots/api#labeledprice) documentation.
 */
@TelegramCodegen.Type
data class LabeledPrice internal constructor(
    /** Portion label. */
    val label: String,
    /** Price of the product in the smallest units of the currency. */
    val amount: Int
) {
    companion object
}
