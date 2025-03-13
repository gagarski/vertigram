package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.WebhookInfo

/**
 * Telegram [getWebHookInfo](https://core.telegram.org/bots/api#getwebhookinfo) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
object GetWebHookInfo : JsonTelegramCallable<WebhookInfo>()
