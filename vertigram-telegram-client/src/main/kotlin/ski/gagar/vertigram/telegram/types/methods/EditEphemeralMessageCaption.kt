package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.HasReceiverUserId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalRichCaption
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to edit captions of ephemeral messages. Returns `true` on success.
 *
 * See Telegram's
 * [editEphemeralMessageCaption](https://core.telegram.org/bots/api#editephemeralmessagecaption) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class EditEphemeralMessageCaption internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the recipient user. */
    override val receiverUserId: Long,
    /** Unique identifier of the ephemeral message. */
    val ephemeralMessageId: Long,
    /** New caption, 0-1024 characters after entities parsing. */
    override val caption: String? = null,
    /** Mode for parsing entities in the new caption. */
    override val parseMode: RichText.ParseMode? = null,
    /** Special entities that appear in the caption; can be specified instead of [parseMode]. */
    override val captionEntities: List<MessageEntity>? = null,
    /** Inline keyboard attached to the message. */
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null
) : JsonTelegramCallable<Boolean>(), HasChatId, HasReceiverUserId, HasOptionalRichCaption
