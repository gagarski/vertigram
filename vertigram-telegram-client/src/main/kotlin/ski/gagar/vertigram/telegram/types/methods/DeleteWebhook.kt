package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Use this method to remove webhook integration if you decide to switch back to
 * [ski.gagar.vertigram.telegram.client.Telegram.getUpdates]. Returns `true` on success.
 *
 * See Telegram's [deleteWebhook](https://core.telegram.org/bots/api#deletewebhook) documentation.
 */
@TelegramCodegen.Method
data class DeleteWebhook internal constructor(
    /** Pass `true` to drop all pending updates. */
    val dropPendingUpdates: Boolean = false
) : JsonTelegramCallable<Boolean>()
