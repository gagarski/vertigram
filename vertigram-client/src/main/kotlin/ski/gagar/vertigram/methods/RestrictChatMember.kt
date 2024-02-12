package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.types.ChatPermissions
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [restrictChatMember](https://core.telegram.org/bots/api#restrictchatmember) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class RestrictChatMember(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val userId: Long,
    val permissions: ChatPermissions,
    val useIndependentChatPermissions: Boolean = false,
    val untilDate: Instant? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
