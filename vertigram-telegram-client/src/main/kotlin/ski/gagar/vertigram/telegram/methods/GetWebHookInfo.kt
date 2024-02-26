package ski.gagar.vertigram.telegram.methods

import ski.gagar.vertigram.telegram.types.WebhookInfo

/**
 * Telegram [getWebHookInfo](https://core.telegram.org/bots/api#getwebhookinfo) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
object GetWebHookInfo : JsonTelegramCallable<WebhookInfo>()
