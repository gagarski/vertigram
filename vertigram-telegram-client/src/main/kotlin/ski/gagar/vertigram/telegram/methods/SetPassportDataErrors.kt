package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.types.Passport
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
