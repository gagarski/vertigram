package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatPermissions

data class SetChatPermissions(
    override val chatId: ChatId,
    val permissions: ChatPermissions,
    // Since Telegram Bot API 6.5
    val useIndependentChatPermissions: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
