package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.telegram.types.StarTransactions

/**
 * Telegram [getStarTransactions](https://core.telegram.org/bots/api#getstartransactions) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetStarTransactions internal constructor(
    val offset: Int? = null,
    val limit: Int? = null,
) : JsonTelegramCallable<StarTransactions>()
