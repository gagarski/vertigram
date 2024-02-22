package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [Story](https://core.telegram.org/bots/api#story) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class Story(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val chat: Chat,
    val id: Long
)
