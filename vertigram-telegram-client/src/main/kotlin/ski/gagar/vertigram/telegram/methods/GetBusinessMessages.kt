package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getBusinessConnection](https://core.telegram.org/bots/api#getbusinessconnection) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
data class GetBusinessConnection(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val businessConnectionId: String
) : JsonTelegramCallable<Boolean>()
