package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [transferGift](https://core.telegram.org/bots/api#transferGift) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class TransferGift internal constructor(
    val businessConnectionId: String,
    val ownedGiftId: String,
    val newOwnerChatId: Long,
    val starCount: Int? = null
) : JsonTelegramCallable<Boolean>()
