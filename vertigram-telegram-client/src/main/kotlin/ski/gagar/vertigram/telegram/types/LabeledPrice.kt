package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [LabeledPrice](https://core.telegram.org/bots/api#labeledprice) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class LabeledPrice(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val label: String,
    val amount: Int
)
