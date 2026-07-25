package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ChatInviteLink
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Instant

/**
 * Use this method to create an additional invite link for a chat. The bot must be an administrator in the chat for
 * this to work and must have the appropriate administrator rights. The link can be revoked using
 * [ski.gagar.vertigram.telegram.methods.revokeChatInviteLink]. Returns the new invite link as [ChatInviteLink].
 *
 * Vertigram exposes separate overloads for invite links with a member limit and invite links that control whether
 * users need to be approved by chat administrators.
 *
 * See Telegram's [createChatInviteLink](https://core.telegram.org/bots/api#createchatinvitelink) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(CreateChatInviteLink.WithMemberLimit::class),
    JsonSubTypes.Type(CreateChatInviteLink.WithJoinRequest::class)
)
@TelegramCodegen.Method
sealed class CreateChatInviteLink : JsonTelegramCallable<ChatInviteLink>(), HasChatId {
    /** Unique identifier for the target chat or username of the target channel. */
    abstract override val chatId: ChatId

    /** Invite link name, 0-32 characters. */
    abstract val name: String?

    /** Point in time when the link will expire. */
    abstract val expireDate: Instant?

    /**
     * Case when [memberLimit] specifies the maximum number of users that can be members of the chat simultaneously
     * after joining through the invite link.
     */
    @TelegramCodegen.Method(
        name = "createChatInviteLink"
    )
    data class WithMemberLimit internal constructor(
        /** Unique identifier for the target chat or username of the target channel. */
        override val chatId: ChatId,
        /** Invite link name, 0-32 characters. */
        override val name: String? = null,
        /** Point in time when the link will expire. */
        override val expireDate: Instant? = null,
        /** The maximum number of users that can be members of the chat simultaneously, 1-99999. */
        val memberLimit: Int
    ) : CreateChatInviteLink() {
        // Intentinally not passed, Telegram will treat that as false and it will help type deduction
        // val createsJoinRequest: Boolean = false
    }

    /**
     * Case when [createsJoinRequest] controls whether users joining through the link need to be approved by chat
     * administrators; a member limit isn't specified.
     */
    @TelegramCodegen.Method(
        name = "createChatInviteLink"
    )
    data class WithJoinRequest internal constructor(
        /** Unique identifier for the target chat or username of the target channel. */
        override val chatId: ChatId,
        /** Invite link name, 0-32 characters. */
        override val name: String? = null,
        /** Point in time when the link will expire. */
        override val expireDate: Instant? = null,
        /** `true` if users joining the chat through the link need to be approved by chat administrators. */
        val createsJoinRequest: Boolean = false
    ) : CreateChatInviteLink()

}
