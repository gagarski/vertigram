package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Telegram [PreparedKeyboardButton](https://core.telegram.org/bots/api#preparedkeyboardbutton) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class PreparedKeyboardButton internal constructor(
    val id: String,
    val expirationDate: Instant
) {
    companion object
}
