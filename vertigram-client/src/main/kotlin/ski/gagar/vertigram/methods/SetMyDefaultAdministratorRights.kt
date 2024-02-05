package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.ChatAdministratorRights

@TgMethod
data class SetMyDefaultAdministratorRights(
    val rights: ChatAdministratorRights? = null,
    val forChannels: Boolean = false
) : JsonTgCallable<Boolean>()
