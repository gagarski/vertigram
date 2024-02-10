package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [close](https://core.telegram.org/bots/api#close) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen(
    methodName = "closeApi",
)
object Close : JsonTelegramCallable<Boolean>()
