package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [sendContact](https://core.telegram.org/bots/api#sendcontact) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen
data class SendContact(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val businessConnectionId: String? = null,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val vcard: String? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val messageEffectId: String? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId
