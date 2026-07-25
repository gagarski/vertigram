package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * This object represents a boost removed from a chat.
 *
 * See Telegram's [ChatBoostRemoved](https://core.telegram.org/bots/api#chatboostremoved) documentation.
 */
@TelegramCodegen.Type
data class ChatBoostRemoved internal constructor(
    /** Chat which was boosted. */
    val chat: Chat,
    /** Unique identifier of the boost. */
    val boostId: String,
    /** Point in time when the boost was removed. */
    val removeDate: Instant,
    /** Source of the removed boost. */
    val source: ChatBoost.Source
) {
    companion object
}
