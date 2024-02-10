package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

/**
 * Telegram [deleteWebhook](https://core.telegram.org/bots/api#deletewebhook) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
object DeleteWebhook : JsonTelegramCallable<Boolean>()
