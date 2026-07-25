package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Describes a keyboard button to be used by a user of a Mini App.
 *
 * See Telegram's [PreparedKeyboardButton](https://core.telegram.org/bots/api#preparedkeyboardbutton) documentation.
 */
@TelegramCodegen.Type
data class PreparedKeyboardButton internal constructor(
    /** Unique identifier of the keyboard button. */
    val id: String,
    /** Expiration date of the keyboard button. */
    val expirationDate: Instant
) {
    companion object
}
