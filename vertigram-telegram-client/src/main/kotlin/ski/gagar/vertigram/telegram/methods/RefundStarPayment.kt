package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [refundStarPayment](https://core.telegram.org/bots/api#refundstarpayment) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class RefundStarPayment(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val userId: Int,
    val telegramPaymentChargeId: String
) : JsonTelegramCallable<Boolean>()
