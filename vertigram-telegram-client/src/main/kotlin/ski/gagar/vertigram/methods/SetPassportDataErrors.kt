package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.Passport
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setPassportDataErrors](https://core.telegram.org/bots/api#setpassportdataerrors) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetPassportDataErrors(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val userId: Long,
    val errors: List<Passport.ElementError>
) : JsonTelegramCallable<Boolean>()
