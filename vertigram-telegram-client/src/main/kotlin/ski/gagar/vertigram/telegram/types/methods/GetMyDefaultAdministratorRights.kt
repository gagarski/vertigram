package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get the current default administrator rights of the bot.
 *
 * See Telegram's
 * [getMyDefaultAdministratorRights](https://core.telegram.org/bots/api#getmydefaultadministratorrights)
 * documentation.
 */
@TelegramCodegen.Method
data class GetMyDefaultAdministratorRights internal constructor(
    /** Pass `true` to get rights for channels; otherwise, rights for groups and supergroups are returned. */
    val forChannels: Boolean = false
) : JsonTelegramCallable<ChatAdministratorRights>()
