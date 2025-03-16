package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [exportChatInviteLink](https://core.telegram.org/bots/api#exportchatinvitelink) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class ExportChatInviteLink internal constructor(
    override val chatId: ChatId
) : JsonTelegramCallable<String>(), HasChatId
