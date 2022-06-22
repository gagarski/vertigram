package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.LabeledPrice
import ski.gagar.vxutil.vertigram.types.Message

@TgMethod
data class CreateInvoiceLink(
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
): JsonTgCallable<Message>()
