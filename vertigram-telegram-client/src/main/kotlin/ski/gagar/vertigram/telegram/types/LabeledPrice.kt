package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [LabeledPrice](https://core.telegram.org/bots/api#labeledprice) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class LabeledPrice internal constructor(
    val label: String,
    val amount: Int
) {
    companion object
}
