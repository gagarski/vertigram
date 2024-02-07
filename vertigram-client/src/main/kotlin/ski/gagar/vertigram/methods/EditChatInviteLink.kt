package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatInviteLink
import ski.gagar.vertigram.util.NoPosArgs
import ski.gagar.vertigram.util.TgMethodName
import ski.gagar.vertigram.util.TgVerticleGenerate
import java.time.Instant

/**
 * Telegram [editChatInviteLink](https://core.telegram.org/bots/api#editchatinvitelink) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TgMethod
data class EditChatInviteLink2(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val inviteLink: String,
    val name: String? = null,
    val expireDate: Instant? = null,
    val memberLimit: Int? = null,
    val createsJoinRequest: Boolean = false
) : JsonTgCallable<ChatInviteLink>(), HasChatId

sealed class EditChatInviteLink {
    /**
     * Case when [memberLimit] is specified, implies that [createsJoinRequest] is false
     */
    @TgMethod(kotlinMethodName = "editChatInviteLink")
    @TgMethodName("editChatInviteLink")
    @TgVerticleGenerate(address = "editChatInviteLinkWithMemberLimit")
    data class WithMemberLimit(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val chatId: ChatId,
        val inviteLink: String,
        val name: String? = null,
        val expireDate: Instant? = null,
        val memberLimit: Int,
    ) : CreateChatInviteLink() {
        val createsJoinRequest: Boolean = false
    }

    /**
     * Case when [memberLimit] is missing, [createsJoinRequest] is specified explicitly
     */
    @TgMethod(kotlinMethodName = "editChatInviteLink")
    @TgMethodName("editChatInviteLink")
    @TgVerticleGenerate(address = "editChatInviteLinkWithJoinRequest")
    data class WithJoinRequest(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val chatId: ChatId,
        val name: String? = null,
        val expireDate: Instant? = null,
        val createsJoinRequest: Boolean = false
    ) : CreateChatInviteLink()

}