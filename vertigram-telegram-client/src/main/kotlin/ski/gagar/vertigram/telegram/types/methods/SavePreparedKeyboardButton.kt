package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.PreparedKeyboardButton
import ski.gagar.vertigram.telegram.types.ReplyMarkup

/**
 * Stores a keyboard button that can be sent by a user of a Mini App.
 *
 * See Telegram's
 * [savePreparedKeyboardButton](https://core.telegram.org/bots/api#savepreparedkeyboardbutton) documentation.
 */
@TelegramCodegen.Method
data class SavePreparedKeyboardButton internal constructor(
    /** Unique identifier of the target user who can use the prepared button. */
    val userId: Long,
    /** Keyboard button to prepare. */
    val button: ReplyMarkup.Keyboard.Button
) : JsonTelegramCallable<PreparedKeyboardButton>()
