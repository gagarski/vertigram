package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.StarAmount

/**
 * Returns the amount of Telegram Stars owned by the bot.
 *
 * See Telegram's [getMyStarBalance](https://core.telegram.org/bots/api#getmystarbalance) documentation.
 */
@TelegramCodegen.Method()
object GetMyStarBalance : JsonTelegramCallable<StarAmount>()
