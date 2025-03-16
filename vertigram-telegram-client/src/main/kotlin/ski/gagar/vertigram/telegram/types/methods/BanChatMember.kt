package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Instant

/**
 * Telegram [banChatMember](https://core.telegram.org/bots/api#banchatmember) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class BanChatMember internal constructor(
    override val chatId: ChatId,
    val userId: Long,
    val untilDate: Instant? = null,
    val revokeMessages: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
