package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.ChatPermissions
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [restrictChatMember](https://core.telegram.org/bots/api#restrictchatmember) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class RestrictChatMember internal constructor(
    override val chatId: ChatId,
    val userId: Long,
    val permissions: ChatPermissions,
    val useIndependentChatPermissions: Boolean = false,
    val untilDate: Instant? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
