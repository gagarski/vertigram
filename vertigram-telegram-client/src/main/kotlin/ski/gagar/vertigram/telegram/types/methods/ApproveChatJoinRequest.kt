package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [approveChatJoinRequest](https://core.telegram.org/bots/api#approvechatjoinrequest) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class ApproveChatJoinRequest internal constructor(
    override val chatId: ChatId,
    val userId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
