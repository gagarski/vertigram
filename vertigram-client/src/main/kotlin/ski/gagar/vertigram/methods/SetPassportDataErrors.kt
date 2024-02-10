package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.PassportElementError

data class SetPassportDataErrors(
    val userId: Long,
    val errors: List<PassportElementError>
) : JsonTelegramCallable<Boolean>()
