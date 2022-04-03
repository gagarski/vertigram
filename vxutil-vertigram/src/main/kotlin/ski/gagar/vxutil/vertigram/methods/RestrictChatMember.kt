package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatPermissions
import java.time.Instant

data class RestrictChatMember(
    val chatId: Long,
    val userId: Long,
    val permissions: ChatPermissions,
    val untilDate: Instant? = null
) : JsonTgCallable<Boolean>()
