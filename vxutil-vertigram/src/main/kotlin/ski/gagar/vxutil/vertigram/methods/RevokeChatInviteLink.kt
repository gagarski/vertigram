package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ChatInviteLink

@TgMethod
data class RevokeChatInviteLink(
    override val chatId: ChatId,
    val inviteLink: String
) : JsonTgCallable<ChatInviteLink>(), HasChatId
