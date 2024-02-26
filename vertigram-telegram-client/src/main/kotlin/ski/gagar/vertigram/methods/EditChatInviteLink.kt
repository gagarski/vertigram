package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatInviteLink
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [editChatInviteLink](https://core.telegram.org/bots/api#editchatinvitelink) method.

 * Subtypes (which are nested) are two mutually-exclusive cases: invite link with member limit and with join request.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(EditChatInviteLink.WithMemberLimit::class),
    JsonSubTypes.Type(EditChatInviteLink.WithJoinRequest::class)
)
sealed class EditChatInviteLink : JsonTelegramCallable<ChatInviteLink>(), HasChatId {
    abstract val inviteLink: String
    abstract val name: String?
    abstract val expireDate: Instant?

    /**
     * Case when [memberLimit] is specified, implies that `createsJoinRequest` is not set
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
        override val inviteLink: String,
        override val name: String? = null,
        override val expireDate: Instant? = null,
        val memberLimit: Int,
    ) : EditChatInviteLink() {
        // Intentinally not passed, Telegram will treat that as false and it will help type deduction
        // val createsJoinRequest: Boolean = false
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
        override val inviteLink: String,
        override val name: String? = null,
        override val expireDate: Instant? = null,
        val createsJoinRequest: Boolean = false
    ) : EditChatInviteLink()

}