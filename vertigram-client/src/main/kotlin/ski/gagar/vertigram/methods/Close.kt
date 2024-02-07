package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

/**
 * Telegram [close](https://core.telegram.org/bots/api#close) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TgMethod(kotlinMethodName = "closeApi")
object Close : JsonTgCallable<Boolean>()
