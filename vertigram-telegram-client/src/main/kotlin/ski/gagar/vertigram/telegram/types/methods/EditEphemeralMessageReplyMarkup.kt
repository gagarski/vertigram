package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.HasReceiverUserId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to edit only the reply markup of ephemeral messages. Returns `true` on success.
 *
 * See Telegram's
 * [editEphemeralMessageReplyMarkup](https://core.telegram.org/bots/api#editephemeralmessagereplymarkup) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class EditEphemeralMessageReplyMarkup internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the recipient user. */
    override val receiverUserId: Long,
    /** Unique identifier of the ephemeral message. */
    val ephemeralMessageId: Long,
    /** Inline keyboard attached to the message. */
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null
) : JsonTelegramCallable<Boolean>(), HasChatId, HasReceiverUserId
