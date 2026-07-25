package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Use this method to close the bot instance before moving it from one local server to another. You need to delete the
 * webhook before calling this method to ensure that the bot isn't launched again after server restart. The method
 * will return error 429 in the first 10 minutes after the bot is launched. Returns `true` on success. Requires no
 * parameters.
 *
 * Vertigram exposes this method as `closeApi` to avoid conflicting with `AutoCloseable.close`.
 *
 * See Telegram's [close](https://core.telegram.org/bots/api#close) documentation.
 */
@TelegramCodegen.Method(
    name = "closeApi",
    telegramName = "close"
)
object Close : JsonTelegramCallable<Boolean>()
