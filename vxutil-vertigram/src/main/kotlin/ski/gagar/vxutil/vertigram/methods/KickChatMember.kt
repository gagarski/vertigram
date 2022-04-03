package ski.gagar.vxutil.vertigram.methods

import java.time.Instant

data class KickChatMember(
    val chatId: Long,
    val userId: Long,
    val untilDate: Instant? = null
) : JsonTgCallable<Boolean>()
