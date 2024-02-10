package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.ChatAdministratorRights

data class GetMyDefaultAdministratorRights(
    val forChannels: Boolean = false
) : JsonTelegramCallable<ChatAdministratorRights>()
