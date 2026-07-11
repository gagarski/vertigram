package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.StarAmount

/**
 * Telegram [getMyStarBalance](https://core.telegram.org/bots/api#getmystarbalance) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
object GetMyStarBalance : JsonTelegramCallable<StarAmount>()
