package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BusinessConnection

/**
 * Use this method to get information about the connection of the bot with a business account.
 *
 * See Telegram's [getBusinessConnection](https://core.telegram.org/bots/api#getbusinessconnection) documentation.
 */
@TelegramCodegen.Method
data class GetBusinessConnection internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String
) : JsonTelegramCallable<BusinessConnection>()
