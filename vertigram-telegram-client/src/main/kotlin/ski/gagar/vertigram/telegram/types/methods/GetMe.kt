package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.User

/**
 * A simple method for testing your bot's authentication token.
 *
 * See Telegram's [getMe](https://core.telegram.org/bots/api#getme) documentation.
 */
@TelegramCodegen.Method
object GetMe : JsonTelegramCallable<User.Me>()
