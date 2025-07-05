package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.Sticker

/**
 * Telegram [editUserStarSubscription](https://core.telegram.org/bots/api#edituserstarsubscription) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class EditUserStarSubscription internal constructor(
    val userId: Long,
    val telegramPaymentChargeId: String,
    @get:JvmName("getIsCancelled")
    val isCancelled: Boolean = false,
) : JsonTelegramCallable<Boolean>()
