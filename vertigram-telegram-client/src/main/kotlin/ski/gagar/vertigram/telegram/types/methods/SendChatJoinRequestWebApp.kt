package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Processes a received chat join request query by showing a Mini App to the user before deciding the outcome.
 *
 * Returns `true` on success.
 *
 * See Telegram's
 * [sendChatJoinRequestWebApp](https://core.telegram.org/bots/api#sendchatjoinrequestwebapp) documentation.
 */
@TelegramCodegen.Method
data class SendChatJoinRequestWebApp internal constructor(
    /** Unique identifier of the join request query. */
    val chatJoinRequestQueryId: String,
    /** HTTPS URL of the Mini App to open. */
    val webAppUrl: String
) : JsonTelegramCallable<Boolean>()
