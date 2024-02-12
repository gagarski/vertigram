package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.types.ChatInviteLink
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [editChatInviteLink](https://core.telegram.org/bots/api#editchatinvitelink) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed class EditChatInviteLink : JsonTelegramCallable<ChatInviteLink>() {
    /**
     * Case when [memberLimit] is specified, implies that [createsJoinRequest] is false
     */
    @TelegramMethod(
        methodName = "editChatInviteLink"
    )
    @TelegramCodegen(
        methodName = "editChatInviteLink",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditChatInviteLink"
    )
    data class WithMemberLimit internal constructor(
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
    @TelegramMethod(
        methodName = "editChatInviteLink"
    )
    @TelegramCodegen(
        methodName = "editChatInviteLink",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditChatInviteLink"
    )
    data class WithJoinRequest internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val chatId: ChatId,
        val name: String? = null,
        val expireDate: Instant? = null,
        val createsJoinRequest: Boolean = false
    ) : CreateChatInviteLink()

}