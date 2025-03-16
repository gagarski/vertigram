package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.LabeledPrice

/**
 * Telegram [createInvoiceLink](https://core.telegram.org/bots/api#createinvoicelink) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class CreateInvoiceLink internal constructor(
    val title: String,
    val description: String,
    val payload: String,
    val providerToken: String? = null,
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
