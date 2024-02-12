package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [banChatMember](https://core.telegram.org/bots/api#banchatmember) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class BanChatMember(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val userId: Long,
    val untilDate: Instant? = null,
    val revokeMessages: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
