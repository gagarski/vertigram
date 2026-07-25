package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.HasReceiverUserId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to edit media of ephemeral messages. Returns `true` on success.
 *
 * See Telegram's
 * [editEphemeralMessageMedia](https://core.telegram.org/bots/api#editephemeralmessagemedia) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class EditEphemeralMessageMedia internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the recipient user. */
    override val receiverUserId: Long,
    /** Unique identifier of the ephemeral message. */
    val ephemeralMessageId: Long,
    /** New media content of the message. */
    val media: InputMedia,
    /** Inline keyboard attached to the message. */
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null
) : JsonTelegramCallable<Boolean>(), HasChatId, HasReceiverUserId
