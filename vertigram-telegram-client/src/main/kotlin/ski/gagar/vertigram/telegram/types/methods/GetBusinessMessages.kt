package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getBusinessConnection](https://core.telegram.org/bots/api#getbusinessconnection) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetBusinessConnection internal constructor(
    val businessConnectionId: String
) : JsonTelegramCallable<Boolean>()
