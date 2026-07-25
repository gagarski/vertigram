package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Gifts

/**
 * Returns the list of gifts that can be sent by the bot to users and channel chats.
 *
 * See Telegram's [getAvailableGifts](https://core.telegram.org/bots/api#getavailablegifts) documentation.
 */
@TelegramCodegen.Method
object GetAvailableGifts : JsonTelegramCallable<Gifts>()
