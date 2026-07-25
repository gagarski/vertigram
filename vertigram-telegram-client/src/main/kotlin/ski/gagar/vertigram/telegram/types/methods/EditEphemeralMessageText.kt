package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.HasReceiverUserId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.richtext.HasRichText
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to edit text of ephemeral messages. Returns `true` on success.
 *
 * See Telegram's
 * [editEphemeralMessageText](https://core.telegram.org/bots/api#editephemeralmessagetext) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class EditEphemeralMessageText internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the recipient user. */
    override val receiverUserId: Long,
    /** Unique identifier of the ephemeral message. */
    val ephemeralMessageId: Long,
    /** New text of the message. */
    override val text: String,
    /** Mode for parsing entities in [text]. */
    override val parseMode: RichText.ParseMode? = null,
    /** Special entities that appear in [text]; can be specified instead of [parseMode]. */
    override val entities: List<MessageEntity>? = null,
    /** Link preview generation options for the message. */
    val linkPreviewOptions: Message.LinkPreviewOptions? = null,
    /** Inline keyboard attached to the message. */
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null
) : JsonTelegramCallable<Boolean>(), HasChatId, HasReceiverUserId, HasRichText
