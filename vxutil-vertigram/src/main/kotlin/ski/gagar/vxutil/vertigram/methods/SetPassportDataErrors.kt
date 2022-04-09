package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.PassportElementError

data class SetPassportDataErrors(
    val userId: Long,
    val errors: List<PassportElementError>
) : JsonTgCallable<Boolean>()
