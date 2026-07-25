package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.telegram.types.ChatInviteLink
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration

/**
 * Use this method to create a subscription invite link for a channel chat. The bot must have the
 * [ChatAdministratorRights.canInviteUsers] administrator rights. The link can be edited using
 * [ski.gagar.vertigram.telegram.methods.editChatSubscriptionInviteLink] or revoked using
 * [ski.gagar.vertigram.telegram.methods.revokeChatInviteLink]. Returns the new invite link as [ChatInviteLink].
 *
 * See Telegram's
 * [createChatSubscriptionInviteLink](https://core.telegram.org/bots/api#createchatsubscriptioninvitelink)
 * documentation.
 */
@TelegramCodegen.Method
data class CreateChatSubscriptionInviteLink internal constructor(
    /** Unique identifier for the target channel chat or username of the target channel. */
    override val chatId: ChatId,
    /** Invite link name, 0-32 characters. */
    val name: String? = null,
    /**
     * The period for which the subscription will be active before the next payment. Telegram currently requires a
     * period of 30 days.
     */
    val subscriptionPeriod: Duration = Defaults.subscriptionPeriod,
    /**
     * The amount of Telegram Stars a user must pay initially and after each subsequent subscription period to be a
     * member of the chat, 1-10000.
     */
    val subscriptionPrice: Int,
) : JsonTelegramCallable<ChatInviteLink>(), HasChatId {
    companion object {
        val THIRTY_DAYS = Duration.ofDays(30)
    }
    object Defaults {
        val subscriptionPeriod = THIRTY_DAYS
    }
}
