package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [ChatBoostUpdated](https://core.telegram.org/bots/api#chatboostupdated) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ChatBoostUpdated(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val chat: Chat,
    val boost: ChatBoost
)