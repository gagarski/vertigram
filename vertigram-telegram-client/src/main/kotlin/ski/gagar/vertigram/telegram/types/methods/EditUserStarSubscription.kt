package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.Sticker

/**
 * Allows the bot to cancel or re-enable extension of a subscription paid in Telegram Stars.
 *
 * Returns `true` on success.
 *
 * See Telegram's
 * [editUserStarSubscription](https://core.telegram.org/bots/api#edituserstarsubscription) documentation.
 */
@TelegramCodegen.Method
data class EditUserStarSubscription internal constructor(
    /** Identifier of the user whose subscription will be edited. */
    val userId: Long,
    /** Telegram payment identifier for the subscription. */
    val telegramPaymentChargeId: String,
    /** Pass `true` to cancel extension of the subscription; pass `false` to re-enable it. */
    @get:JvmName("getIsCanceled")
    val isCanceled: Boolean = false,
) : JsonTelegramCallable<Boolean>()
