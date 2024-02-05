package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.ChatAdministratorRights

@TgMethod
data class GetMyDefaultAdministratorRights(
    val forChannels: Boolean = false
) : JsonTgCallable<ChatAdministratorRights>()
