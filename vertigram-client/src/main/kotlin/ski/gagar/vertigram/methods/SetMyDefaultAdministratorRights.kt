package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.ChatAdministratorRights

data class SetMyDefaultAdministratorRights(
    val rights: ChatAdministratorRights? = null,
    val forChannels: Boolean = false
) : JsonTelegramCallable<Boolean>()
