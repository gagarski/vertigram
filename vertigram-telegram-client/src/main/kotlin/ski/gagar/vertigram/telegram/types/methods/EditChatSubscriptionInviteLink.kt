package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.ChatInviteLink
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration

/**
 * Use this method to edit a subscription invite link created by the bot.
 *
 * The bot must have
 * [ski.gagar.vertigram.telegram.types.ChatAdministratorRights.canInviteUsers] administrator rights.
 *
 * See Telegram's
 * [editChatSubscriptionInviteLink](https://core.telegram.org/bots/api#editchatsubscriptioninvitelink)
 * documentation.
 */
@TelegramCodegen.Method
data class EditChatSubscriptionInviteLink internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Invite link to edit. */
    val inviteLink: String,
    /** Invite link name, 0-32 characters. */
    val name: String? = null
) : JsonTelegramCallable<ChatInviteLink>(), HasChatId
