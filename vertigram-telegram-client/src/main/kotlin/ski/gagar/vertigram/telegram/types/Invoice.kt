package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [Invoice](https://core.telegram.org/bots/api#invoice) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class Invoice(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val title: String,
    val description: String,
    val startParameter: String,
    val currency: String,
    val totalAmount: Int
)
