package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [close](https://core.telegram.org/bots/api#close) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method(
    name = "closeApi",
    telegramName = "close"
)
object Close : JsonTelegramCallable<Boolean>()
