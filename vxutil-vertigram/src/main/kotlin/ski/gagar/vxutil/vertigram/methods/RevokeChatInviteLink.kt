package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ChatInviteLink
import ski.gagar.vxutil.vertigram.types.ChatPermissions
import java.time.Instant

data class RevokeChatInviteLink(
    val chatId: ChatId,
    val inviteLink: String
) : JsonTgCallable<ChatInviteLink>()
