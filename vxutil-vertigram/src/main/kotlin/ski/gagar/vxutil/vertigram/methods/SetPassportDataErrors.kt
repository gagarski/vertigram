package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.PassportElementError

@TgMethod
data class SetPassportDataErrors(
    val userId: Long,
    val errors: List<PassportElementError>
) : JsonTgCallable<Boolean>
