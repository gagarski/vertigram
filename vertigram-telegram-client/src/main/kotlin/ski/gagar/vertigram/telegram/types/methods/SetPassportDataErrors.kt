package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Passport
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setPassportDataErrors](https://core.telegram.org/bots/api#setpassportdataerrors) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetPassportDataErrors internal constructor(
    val userId: Long,
    val errors: List<Passport.ElementError>
) : JsonTelegramCallable<Boolean>()
