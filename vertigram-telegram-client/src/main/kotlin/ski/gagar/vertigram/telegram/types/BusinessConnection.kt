package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Telegram [BusinessConnection](https://core.telegram.org/bots/api#businessconnection) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class BusinessConnection internal constructor(
    val id: String,
    val user: User,
    val userChatId: Long,
    val date: Instant,
    val canReply: Boolean,
    @get:JvmName("getIsEnabled")
    val isEnabled: Boolean
) {
    companion object
}
