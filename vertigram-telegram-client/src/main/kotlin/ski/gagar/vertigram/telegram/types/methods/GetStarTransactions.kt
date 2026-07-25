package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.telegram.types.StarTransactions

/**
 * Returns the bot's Telegram Star transactions in chronological order.
 *
 * See Telegram's [getStarTransactions](https://core.telegram.org/bots/api#getstartransactions) documentation.
 */
@TelegramCodegen.Method
data class GetStarTransactions internal constructor(
    /** Number of transactions to skip in the response. */
    val offset: Int? = null,
    /** Maximum number of transactions to retrieve; 1-100. */
    val limit: Int? = null,
) : JsonTelegramCallable<StarTransactions>()
