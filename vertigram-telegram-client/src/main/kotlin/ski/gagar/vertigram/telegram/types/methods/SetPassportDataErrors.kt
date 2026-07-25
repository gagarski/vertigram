package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Passport
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Informs a user that some Telegram Passport elements they provided contain errors.
 *
 * Returns `true` on success.
 *
 * See Telegram's [setPassportDataErrors](https://core.telegram.org/bots/api#setpassportdataerrors) documentation.
 */
@TelegramCodegen.Method
data class SetPassportDataErrors internal constructor(
    /** User identifier. */
    val userId: Long,
    /** Errors describing the problematic Telegram Passport elements. */
    val errors: List<Passport.ElementError>
) : JsonTelegramCallable<Boolean>()
