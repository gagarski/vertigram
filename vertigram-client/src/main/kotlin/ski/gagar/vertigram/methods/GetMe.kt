package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.Me

/**
 * Telegram [getMe](https://core.telegram.org/bots/api#getme) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
object GetMe : JsonTelegramCallable<Me>()
