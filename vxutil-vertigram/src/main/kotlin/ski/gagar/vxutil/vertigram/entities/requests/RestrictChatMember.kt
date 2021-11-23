package ski.gagar.vxutil.vertigram.entities.requests

import ski.gagar.vxutil.vertigram.entities.ChatPermissions
import java.time.Instant

data class RestrictChatMember(
    val chatId: Long,
    val userId: Long,
    val permissions: ChatPermissions,
    val untilDate: Instant? = null
) : JsonTgCallable<Boolean>()
