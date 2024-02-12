package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [ChatBoostRemoved](https://core.telegram.org/bots/api#chatboostremoved) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ChatBoostRemoved(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val chat: Chat,
    val boostId: String,
    val removeDate: Instant,
    val source: ChatBoost.Source
)