package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ChatInviteLink
import ski.gagar.vxutil.vertigram.types.ChatPermissions
import java.time.Instant

data class EditChatInviteLink(
    val chatId: ChatId,
    val inviteLink: String,
    val name: String? = null,
    val expireDate: Instant? = null,
    val memberLimit: Long? = null,
    val createsJoinRequest: Boolean = false
) : JsonTgCallable<ChatInviteLink>()
