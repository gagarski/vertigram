package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMethod
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [stopMessageLiveLocation](https://core.telegram.org/bots/api#stopmessagelivelocation) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(StopMessageLiveLocation.InlineMessage::class),
    JsonSubTypes.Type(StopMessageLiveLocation.ChatMessage::class)
)
sealed interface StopMessageLiveLocation {
    val businessConnectionId: String?
    val replyMarkup: ReplyMarkup?
    /**
     * Inline message case
     */
    @TelegramMethod(
        methodName = "stopMessageLiveLocation"
    )
    @TelegramCodegen.Method(
        name = "stopMessageLiveLocation"
    )
    data class InlineMessage internal constructor(
        override val businessConnectionId: String? = null,
        val inlineMessageId: String,
        override val replyMarkup: ReplyMarkup? = null
    ) : JsonTelegramCallable<Boolean>(), StopMessageLiveLocation

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "stopMessageLiveLocation"
    )
    @TelegramCodegen.Method(
        name = "stopMessageLiveLocation"
    )
    data class ChatMessage internal constructor(
        override val businessConnectionId: String? = null,
        override val chatId: ChatId,
        val messageId: Long,
        override val replyMarkup: ReplyMarkup? = null
    ) : JsonTelegramCallable<Boolean>(), StopMessageLiveLocation, HasChatId
}