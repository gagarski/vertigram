package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ShippingOption

@TgMethod
data class AnswerShippingQuery(
    val shippingQueryId: String,
    val ok: Boolean,
    val shippingOptions: List<ShippingOption>? = null,
    val errorMessage: String? = null
) : JsonTgCallable<Boolean>
