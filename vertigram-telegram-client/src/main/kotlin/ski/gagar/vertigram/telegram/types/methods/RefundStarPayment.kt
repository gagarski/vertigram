package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [refundStarPayment](https://core.telegram.org/bots/api#refundstarpayment) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class RefundStarPayment internal constructor(
    val userId: Long,
    val telegramPaymentChargeId: String
) : JsonTelegramCallable<Boolean>()
