package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatInviteLink
import ski.gagar.vertigram.util.NoPosArgs
import ski.gagar.vertigram.util.TgMethodName
import ski.gagar.vertigram.util.TgVerticleGenerate
import java.time.Instant

/**
 * Telegram [createChatInviteLink](https://core.telegram.org/bots/api#createchatinvitelink) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed class CreateChatInviteLink : JsonTgCallable<ChatInviteLink>(), HasChatId {
    /**
     * Case when [memberLimit] is specified, implies that [createsJoinRequest] is false
     */
    @TgMethod
    @TgMethodName("createChatInviteLink")
    @TgVerticleGenerate(address = "createChatInviteLinkWithMemberLimit")
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
    @TgMethod
    @TgMethodName("createChatInviteLink")
    @TgVerticleGenerate(address = "createChatInviteLinkWithJoinRequest")
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