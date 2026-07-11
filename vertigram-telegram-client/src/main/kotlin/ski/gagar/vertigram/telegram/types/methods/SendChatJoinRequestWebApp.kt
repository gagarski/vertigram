package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [sendChatJoinRequestWebApp](https://core.telegram.org/bots/api#sendchatjoinrequestwebapp) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SendChatJoinRequestWebApp internal constructor(
    val chatJoinRequestQueryId: String,
    val webAppUrl: String
) : JsonTelegramCallable<Boolean>()
