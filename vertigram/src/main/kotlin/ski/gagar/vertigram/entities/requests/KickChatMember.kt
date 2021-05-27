package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.annotations.TgMethod
import java.time.Instant

@TgMethod
data class KickChatMember(
    val chatId: Long,
    val userId: Long,
    val untilDate: Instant? = null
) : JsonTgCallable<Boolean>()
