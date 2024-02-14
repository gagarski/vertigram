package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatInviteLink
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [createChatInviteLink](https://core.telegram.org/bots/api#createchatinvitelink) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: invite link with member limit and with join request.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed class CreateChatInviteLink : JsonTelegramCallable<ChatInviteLink>(), HasChatId {
    /**
     * Case when [memberLimit] is specified, implies that [createsJoinRequest] is false
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
        val name: String? = null,
        val expireDate: Instant? = null,
        val memberLimit: Int
    ) : CreateChatInviteLink() {
        val createsJoinRequest: Boolean = false
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
        val name: String? = null,
        val expireDate: Instant? = null,
        val createsJoinRequest: Boolean = false
    ) : CreateChatInviteLink()

}