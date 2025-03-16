package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.User

/**
 * Telegram [getMe](https://core.telegram.org/bots/api#getme) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
object GetMe : JsonTelegramCallable<User.Me>()
