package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [deleteBusinessMessages](https://core.telegram.org/bots/api#deletebusinessmessages) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class DeleteBusinessMessages internal constructor(
    val businessConnectionId: String,
    val messageIds: List<Long>
) : JsonTelegramCallable<Boolean>()
