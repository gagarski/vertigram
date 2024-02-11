package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.LabeledPrice
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [createInvoiceLink](https://core.telegram.org/bots/api#createinvoicelink) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class CreateInvoiceLink(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val title: String,
    val description: String,
    val payload: String,
    val providerToken: String,
    val currency: String,
    val prices: List<LabeledPrice>,
    val maxTipAmount: Int? = null,
    val suggestedTipAmounts: List<Int>? = null,
    val providerData: String? = null,
    val photoUrl: String? = null,
    val photoSize: Long? = null,
    val photoWidth: Int? = null,
    val photoHeight: Int? = null,
    val needName: Boolean = false,
    val needPhoneNumber: Boolean = false,
    val needEmail: Boolean = false,
    val needShippingAddress: Boolean = false,
    val sendPhoneNumberToProvider: Boolean = false,
    val sendEmailToProvider: Boolean = false,
    @get:JvmName("getIsFlexible")
    val isFlexible: Boolean
): JsonTelegramCallable<String>()
