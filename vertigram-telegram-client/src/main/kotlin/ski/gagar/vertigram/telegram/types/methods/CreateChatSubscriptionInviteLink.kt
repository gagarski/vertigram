package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.ChatInviteLink
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration

/**
 * Telegram [createChatSubscriptionInviteLink](https://core.telegram.org/bots/api#createchatsubscriptioninvitelink) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class CreateChatSubscriptionInviteLink internal constructor(
    override val chatId: ChatId,
    val name: String? = null,
    val subscriptionPeriod: Duration = Defaults.subscriptionPeriod,
    val subscriptionPrice: Int,
) : JsonTelegramCallable<ChatInviteLink>(), HasChatId {
    companion object {
        val THIRTY_DAYS = Duration.ofDays(30)
    }
    object Defaults {
        val subscriptionPeriod = THIRTY_DAYS
    }
}
