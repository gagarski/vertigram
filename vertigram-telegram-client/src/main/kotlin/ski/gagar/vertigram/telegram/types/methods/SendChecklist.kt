package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong
import ski.gagar.vertigram.telegram.types.InputChecklist
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters

/**
 * Use this method to send a checklist on behalf of a connected business account.
 *
 * See Telegram's [sendChecklist](https://core.telegram.org/bots/api#sendchecklist) documentation.
 */
@TelegramCodegen.Method()
data class SendChecklist internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be sent. */
    val businessConnectionId: String,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: Long,
    /** Checklist to send. */
    val checklist: InputChecklist,
    /** Sends the message silently. */
    val disableNotification: Boolean = false,
    /** Protects the sent message from forwarding and saving. */
    val protectContent: Boolean = false,
    /** Unique identifier of the message effect added to the message. */
    val messageEffectId: String? = null,
    /** Parameters of the message being replied to. */
    val replyParameters: ReplyParameters? = null,
    /** Additional interface options. */
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Boolean>(), HasChatIdLong
