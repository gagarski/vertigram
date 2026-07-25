package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change the default administrator rights requested by the bot when added as an administrator.
 *
 * Returns `true` on success.
 *
 * See Telegram's
 * [setMyDefaultAdministratorRights](https://core.telegram.org/bots/api#setmydefaultadministratorrights)
 * documentation.
 */
@TelegramCodegen.Method
data class SetMyDefaultAdministratorRights internal constructor(
    /** New default administrator rights; pass `null` to clear them. */
    val rights: ChatAdministratorRights? = null,
    /** Pass `true` to change rights for channels, or `false` for groups and supergroups. */
    val forChannels: Boolean = false
) : JsonTelegramCallable<Boolean>()
