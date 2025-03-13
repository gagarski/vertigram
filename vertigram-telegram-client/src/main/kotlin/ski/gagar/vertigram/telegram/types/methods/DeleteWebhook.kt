package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [deleteWebhook](https://core.telegram.org/bots/api#deletewebhook) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
object DeleteWebhook : JsonTelegramCallable<Boolean>()
