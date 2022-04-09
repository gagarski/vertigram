package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import java.time.Instant

data class UnbanChatMember(
    val chatId: ChatId,
    val userId: Long,
    val onlyIfBanned: Boolean = false
) : JsonTgCallable<Boolean>()
