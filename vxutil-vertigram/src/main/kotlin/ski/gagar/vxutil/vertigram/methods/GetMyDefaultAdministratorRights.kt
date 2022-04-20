package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatAdministratorRights

@TgMethod
data class GetMyDefaultAdministratorRights(
    val forChannels: Boolean = false
) : JsonTgCallable<ChatAdministratorRights>()
