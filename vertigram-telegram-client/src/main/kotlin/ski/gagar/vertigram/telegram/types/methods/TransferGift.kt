package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Transfers an owned unique gift to another user. Returns `true` on success.
 *
 * See Telegram's [transferGift](https://core.telegram.org/bots/api#transfergift) documentation.
 */
@TelegramCodegen.Method()
data class TransferGift internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** Unique identifier of the gift to transfer. */
    val ownedGiftId: String,
    /** Unique identifier of the chat which will own the gift. */
    val newOwnerChatId: Long,
    /** Number of Telegram Stars paid for the transfer from the business account balance. */
    val starCount: Int? = null
) : JsonTelegramCallable<Boolean>()
