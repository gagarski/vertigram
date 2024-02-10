package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.ParseMode
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.ReplyParameters
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [copyMessage](https://core.telegram.org/bots/api#copymessage) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen(
    generatePseudoConstructor = true
)
@Throttled
data class CopyMessage internal constructor(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val fromChatId: ChatId,
    val messageId: Long,
    @JsonProperty("caption")
    val captionUnwrapped: String? = null,
    @JsonProperty("parse_mode")
    val captionParseModeUnwrapped: ParseMode? = null,
    @JsonProperty("caption_entities")
    val captionEntitiesUnwrapped: List<MessageEntity>? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId

