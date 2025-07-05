package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [StarTransactions](https://core.telegram.org/bots/api#startransactions) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class StarTransactions internal constructor(
    val transactions: List<StarTransaction>
) {
    companion object
}