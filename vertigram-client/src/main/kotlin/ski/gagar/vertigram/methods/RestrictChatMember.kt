package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatPermissions
import java.time.Instant

data class RestrictChatMember(
    override val chatId: ChatId,
    val userId: Long,
    val permissions: ChatPermissions,
    val untilDate: Instant? = null,
    // Since Telegram Bot API 6.5
    val useIndependentChatPermissions: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
