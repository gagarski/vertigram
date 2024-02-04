package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatInviteLink

@TgMethod
data class RevokeChatInviteLink(
    override val chatId: ChatId,
    val inviteLink: String
) : JsonTgCallable<ChatInviteLink>(), HasChatId
