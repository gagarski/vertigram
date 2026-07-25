package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.WebhookInfo

/**
 * Use this method to get the current webhook status.
 *
 * If the bot is using [ski.gagar.vertigram.telegram.client.Telegram.getUpdates], the returned object has an empty URL.
 *
 * See Telegram's [getWebhookInfo](https://core.telegram.org/bots/api#getwebhookinfo) documentation.
 */
@TelegramCodegen.Method
object GetWebHookInfo : JsonTelegramCallable<WebhookInfo>()
