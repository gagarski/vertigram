package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getMyDefaultAdministratorRights](https://core.telegram.org/bots/api#getmydefaultadministratorrights) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class GetMyDefaultAdministratorRights(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val forChannels: Boolean = false
) : JsonTelegramCallable<ChatAdministratorRights>()
