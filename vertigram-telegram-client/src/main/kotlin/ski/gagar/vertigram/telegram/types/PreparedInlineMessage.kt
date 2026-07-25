package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Describes an inline message to be sent by a user of a Mini App.
 *
 * See Telegram's [PreparedInlineMessage](https://core.telegram.org/bots/api#preparedinlinemessage) documentation.
 */
@TelegramCodegen.Type
data class PreparedInlineMessage internal constructor(
    /** Unique identifier of the prepared message. */
    val id: String,
    /** Expiration date of the prepared message. Expired prepared messages can no longer be used. */
    val expirationDate: Instant
) {
    companion object
}
