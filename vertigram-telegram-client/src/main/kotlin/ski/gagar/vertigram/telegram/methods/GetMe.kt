package ski.gagar.vertigram.telegram.methods

import ski.gagar.vertigram.telegram.types.User

/**
 * Telegram [getMe](https://core.telegram.org/bots/api#getme) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
object GetMe : JsonTelegramCallable<User.Me>()
