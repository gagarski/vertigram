package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Refunds a successful payment in Telegram Stars. Returns `true` on success.
 *
 * See Telegram's [refundStarPayment](https://core.telegram.org/bots/api#refundstarpayment) documentation.
 */
@TelegramCodegen.Method
data class RefundStarPayment internal constructor(
    /** Identifier of the user whose payment will be refunded. */
    val userId: Long,
    /** Telegram payment identifier. */
    val telegramPaymentChargeId: String
) : JsonTelegramCallable<Boolean>()
