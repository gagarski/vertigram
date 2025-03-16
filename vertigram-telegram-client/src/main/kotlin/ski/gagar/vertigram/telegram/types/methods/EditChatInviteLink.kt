package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMethod
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ChatInviteLink
import ski.gagar.vertigram.telegram.types.util.ChatId
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
@TelegramCodegen.Method
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
    @TelegramCodegen.Method(
        name = "editChatInviteLink"
    )
    data class WithMemberLimit internal constructor(
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
    @TelegramCodegen.Method(
        name = "editChatInviteLink"
    )
    data class WithJoinRequest internal constructor(
        override val chatId: ChatId,
        override val inviteLink: String,
        override val name: String? = null,
        override val expireDate: Instant? = null,
        val createsJoinRequest: Boolean = false
    ) : EditChatInviteLink()

}