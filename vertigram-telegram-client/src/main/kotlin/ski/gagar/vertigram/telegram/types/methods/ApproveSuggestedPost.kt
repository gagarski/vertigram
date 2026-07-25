package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Use this method to approve a suggested post in a direct messages chat. The bot must have the `can_post_messages`
 * administrator right in the corresponding channel chat. Returns `true` on success.
 *
 * See Telegram's [approveSuggestedPost](https://core.telegram.org/bots/api#approvesuggestedpost) documentation.
 */
@TelegramCodegen.Method
data class ApproveSuggestedPost internal constructor(
    /** Unique identifier for the target direct messages chat. */
    val chatId: Long,
    /** Identifier of a suggested post message to approve. */
    val messageId: Long,
    /**
     * Point in time when the post is expected to be published; omit if the date has already been specified when the
     * suggested post was created. If specified, then the date must be not more than 30 days in the future.
     */
    val sendDate: Instant? = null
) : JsonTelegramCallable<Boolean>()
