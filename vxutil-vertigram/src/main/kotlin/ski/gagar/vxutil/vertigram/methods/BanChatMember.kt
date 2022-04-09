package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import java.time.Instant

data class BanChatMember(
    val chatId: ChatId,
    val userId: Long,
    val untilDate: Instant? = null,
    val revokeMessages: Boolean = false
) : JsonTgCallable<Boolean>()
