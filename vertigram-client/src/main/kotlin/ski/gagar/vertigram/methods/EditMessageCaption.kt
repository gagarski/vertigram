package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.richtext.RichCaption
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.*
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [editMessageCaption](https://core.telegram.org/bots/api#editmessagecaption) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TgMethod
@Throttled
data class EditMessageCaption(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: Long? = null,
    private val caption: String? = null,
    private val parseMode: ParseMode? = null,
    private val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>(), HasChatId {
//    val caption by lazy {
//        caption_?.let {
//            RichCaption.from(it, parseMode, captionEntities)
//        }
//    }
}
