package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatInviteLink

data class RevokeChatInviteLink(
    override val chatId: ChatId,
    val inviteLink: String
) : JsonTelegramCallable<ChatInviteLink>(), HasChatId
