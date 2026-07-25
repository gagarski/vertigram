package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to stop updating a live location message before its live period expires.
 *
 * See Telegram's
 * [stopMessageLiveLocation](https://core.telegram.org/bots/api#stopmessagelivelocation) documentation.
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
     * Case when the message is an inline message. Returns `true` on success.
     */
    @TelegramCodegen.Method(
        name = "stopMessageLiveLocation"
    )
    data class InlineMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Identifier of the inline message. */
        val inlineMessageId: String,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup? = null
    ) : JsonTelegramCallable<Boolean>(), StopMessageLiveLocation

    /**
     * Case when the message belongs to a chat. Returns the edited message on success.
     */
    @TelegramCodegen.Method(
        name = "stopMessageLiveLocation"
    )
    data class ChatMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
        override val chatId: ChatId,
        /** Identifier of the message with the live location to stop. */
        val messageId: Long,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup? = null
    ) : JsonTelegramCallable<Boolean>(), StopMessageLiveLocation, HasChatId
}
