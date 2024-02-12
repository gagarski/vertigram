package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [ChatShared](https://core.telegram.org/bots/api#chatshared) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ChatShared(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val requestId: Long,
    val chatId: Long
)
