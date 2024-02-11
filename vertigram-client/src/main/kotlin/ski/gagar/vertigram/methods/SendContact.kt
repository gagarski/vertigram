package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.ReplyParameters
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [sendContact](https://core.telegram.org/bots/api#sendcontact) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class SendContact(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val vcard: String? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId
