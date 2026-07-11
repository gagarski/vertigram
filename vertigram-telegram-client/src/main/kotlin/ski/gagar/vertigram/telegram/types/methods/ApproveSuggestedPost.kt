package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Telegram [approveSuggestedPost](https://core.telegram.org/bots/api#approvesuggestedpost) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class ApproveSuggestedPost internal constructor(
    val chatId: Long,
    val messageId: Long,
    val sendDate: Instant? = null
) : JsonTelegramCallable<Boolean>()
