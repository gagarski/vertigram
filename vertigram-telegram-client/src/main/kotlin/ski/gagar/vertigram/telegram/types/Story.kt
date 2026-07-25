package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Represents a story.
 *
 * See Telegram's [Story](https://core.telegram.org/bots/api#story) documentation.
 */
@TelegramCodegen.Type
data class Story internal constructor(
    /** Chat that posted the story. */
    val chat: Chat,
    /** Unique identifier for the story in the chat. */
    val id: Long
) {
    companion object
}
