package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.ChatInviteLink
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration

/**
 * Telegram [editChatSubscriptionInviteLink](https://core.telegram.org/bots/api#editchatsubscriptioninvitelink) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class EditChatSubscriptionInviteLink internal constructor(
    override val chatId: ChatId,
    val inviteLink: String,
    val name: String? = null
) : JsonTelegramCallable<ChatInviteLink>(), HasChatId
