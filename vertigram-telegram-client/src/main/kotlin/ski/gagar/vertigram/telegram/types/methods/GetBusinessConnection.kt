package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BusinessConnection

/**
 * Telegram [getBusinessConnection](https://core.telegram.org/bots/api#getbusinessconnection) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetBusinessConnection internal constructor(
    val businessConnectionId: String
) : JsonTelegramCallable<BusinessConnection>()
