package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ChatPermissions
import java.time.Instant

data class RestrictChatMember(
    val chatId: ChatId,
    val userId: Long,
    val permissions: ChatPermissions,
    val untilDate: Instant? = null
) : JsonTgCallable<Boolean>()
