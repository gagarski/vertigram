package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getMyDefaultAdministratorRights](https://core.telegram.org/bots/api#getmydefaultadministratorrights) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetMyDefaultAdministratorRights internal constructor(
    val forChannels: Boolean = false
) : JsonTelegramCallable<ChatAdministratorRights>()
