package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [banChatSenderChat](https://core.telegram.org/bots/api#banchatsenderchat) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class BanChatSenderChat internal constructor(
    override val chatId: ChatId,
    val senderChatId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
