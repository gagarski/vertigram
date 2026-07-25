package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ChatInviteLink
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Instant

/**
 * Use this method to edit a non-primary invite link created by the bot.
 *
 * The bot must be an administrator in the chat for this to work and must have appropriate administrator rights.
 *
 * See Telegram's [editChatInviteLink](https://core.telegram.org/bots/api#editchatinvitelink) documentation.
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
     * Case when the invite link limits the number of simultaneous chat members.
     */
    @TelegramCodegen.Method(
        name = "editChatInviteLink"
    )
    data class WithMemberLimit internal constructor(
        /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
        override val chatId: ChatId,
        /** Invite link to edit. */
        override val inviteLink: String,
        /** Invite link name, 0-32 characters. */
        override val name: String? = null,
        /** Point in time when the link will expire. */
        override val expireDate: Instant? = null,
        /** Maximum number of users that can be chat members after joining through this link; 1-99999. */
        val memberLimit: Int,
    ) : EditChatInviteLink() {
        // Intentinally not passed, Telegram will treat that as false and it will help type deduction
        // val createsJoinRequest: Boolean = false
    }

    /**
     * Case when the invite link can require approval by chat administrators.
     */
    @TelegramCodegen.Method(
        name = "editChatInviteLink"
    )
    data class WithJoinRequest internal constructor(
        /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
        override val chatId: ChatId,
        /** Invite link to edit. */
        override val inviteLink: String,
        /** Invite link name, 0-32 characters. */
        override val name: String? = null,
        /** Point in time when the link will expire. */
        override val expireDate: Instant? = null,
        /** Pass `true` if users joining through this link need approval by chat administrators. */
        val createsJoinRequest: Boolean = false
    ) : EditChatInviteLink()

}
