package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Gifts

/**
 * Telegram [getAvailableGifts](https://core.telegram.org/bots/api#getavailablegifts) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
object GetAvailableGifts : JsonTelegramCallable<Gifts>()
