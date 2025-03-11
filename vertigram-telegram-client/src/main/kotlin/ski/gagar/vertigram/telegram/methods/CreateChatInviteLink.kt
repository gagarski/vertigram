package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMethod
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ChatInviteLink
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [createChatInviteLink](https://core.telegram.org/bots/api#createchatinvitelink) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: invite link with member limit and with join request.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(CreateChatInviteLink.WithMemberLimit::class),
    JsonSubTypes.Type(CreateChatInviteLink.WithJoinRequest::class)
)
@TelegramCodegen
sealed class CreateChatInviteLink : JsonTelegramCallable<ChatInviteLink>(), HasChatId {
    abstract val name: String?
    abstract val expireDate: Instant?
    /**
     * Case when [memberLimit] is specified, implies that `createsJoinRequest` is not set
     */
    @TelegramMethod(
        methodName = "createChatInviteLink"
    )
    @TelegramCodegen(
        methodName = "createChatInviteLink",
        generatePseudoConstructor = true,
        pseudoConstructorName = "CreateChatInviteLink"
    )
    data class WithMemberLimit internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val chatId: ChatId,
        override val name: String? = null,
        override val expireDate: Instant? = null,
        val memberLimit: Int
    ) : CreateChatInviteLink() {
        // Intentinally not passed, Telegram will treat that as false and it will help type deduction
        // val createsJoinRequest: Boolean = false
    }

    /**
     * Case when [memberLimit] is missing, [createsJoinRequest] is specified explicitly
     */
    @TelegramMethod(
        methodName = "createChatInviteLink"
    )
    @TelegramCodegen(
        methodName = "createChatInviteLink",
        generatePseudoConstructor = true,
        pseudoConstructorName = "CreateChatInviteLink"
    )
    data class WithJoinRequest internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val chatId: ChatId,
        override val name: String? = null,
        override val expireDate: Instant? = null,
        val createsJoinRequest: Boolean = false
    ) : CreateChatInviteLink()

}