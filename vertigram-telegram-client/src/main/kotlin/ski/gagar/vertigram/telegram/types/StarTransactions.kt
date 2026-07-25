package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Contains a list of Telegram Star transactions.
 *
 * See Telegram's [StarTransactions](https://core.telegram.org/bots/api#startransactions) documentation.
 */
@TelegramCodegen.Type
data class StarTransactions internal constructor(
    /** List of transactions. */
    val transactions: List<StarTransaction>
) {
    companion object
}
