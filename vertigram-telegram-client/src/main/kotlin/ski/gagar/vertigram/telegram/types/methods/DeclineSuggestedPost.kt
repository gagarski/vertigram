package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights

/**
 * Use this method to decline a suggested post in a direct messages chat. The bot must have the
 * [ChatAdministratorRights.canManageDirectMessages] administrator right in the corresponding channel chat. Returns
 * `true` on success.
 *
 * See Telegram's [declineSuggestedPost](https://core.telegram.org/bots/api#declinesuggestedpost) documentation.
 */
@TelegramCodegen.Method
data class DeclineSuggestedPost internal constructor(
    /** Unique identifier for the target direct messages chat. */
    val chatId: Long,
    /** Identifier of a suggested post message to decline. */
    val messageId: Long,
    /** Comment for the creator of the suggested post, 0-128 characters. */
    val comment: String? = null
) : JsonTelegramCallable<Boolean>()
