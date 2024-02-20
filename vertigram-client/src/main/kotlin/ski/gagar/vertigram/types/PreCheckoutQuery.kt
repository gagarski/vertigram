package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [PreCheckoutQuery](https://core.telegram.org/bots/api#precheckoutquery) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class PreCheckoutQuery(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val id: String,
    val from: User,
    val currency: String,
    val totalAmount: Int,
    val invoicePayload: String,
    val shippingOptionId: String? = null,
    val orderInfo: OrderInfo? = null
)
