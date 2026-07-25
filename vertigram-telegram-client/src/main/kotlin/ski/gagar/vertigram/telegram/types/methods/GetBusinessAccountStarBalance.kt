package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.StarAmount

/**
 * Returns the amount of Telegram Stars owned by a managed business account.
 *
 * See Telegram's
 * [getBusinessAccountStarBalance](https://core.telegram.org/bots/api#getbusinessaccountstarbalance) documentation.
 */
@TelegramCodegen.Method()
data class GetBusinessAccountStarBalance internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String
) : JsonTelegramCallable<StarAmount>()
