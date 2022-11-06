package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ChatInviteLink
import java.time.Instant

@TgMethod
data class EditChatInviteLink(
    override val chatId: ChatId,
    val inviteLink: String,
    val name: String? = null,
    val expireDate: Instant? = null,
    val memberLimit: Int? = null,
    val createsJoinRequest: Boolean = false
) : JsonTgCallable<ChatInviteLink>(), HasChatId
