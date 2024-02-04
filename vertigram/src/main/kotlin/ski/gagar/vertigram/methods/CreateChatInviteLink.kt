package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatInviteLink
import java.time.Instant

@TgMethod
data class CreateChatInviteLink(
    override val chatId: ChatId,
    val name: String? = null,
    val expireDate: Instant? = null,
    val memberLimit: Int? = null,
    val createsJoinRequest: Boolean = false
) : JsonTgCallable<ChatInviteLink>(), HasChatId
