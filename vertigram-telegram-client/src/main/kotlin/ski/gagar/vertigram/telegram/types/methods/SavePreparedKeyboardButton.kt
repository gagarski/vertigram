package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.PreparedKeyboardButton
import ski.gagar.vertigram.telegram.types.ReplyMarkup

/**
 * Telegram [savePreparedKeyboardButton](https://core.telegram.org/bots/api#savepreparedkeyboardbutton) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SavePreparedKeyboardButton internal constructor(
    val userId: Long,
    val button: ReplyMarkup.Keyboard.Button
) : JsonTelegramCallable<PreparedKeyboardButton>()
