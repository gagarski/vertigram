package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [stopMessageLiveLocation](https://core.telegram.org/bots/api#stopmessagelivelocation) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed interface StopMessageLiveLocation {
    val replyMarkup: ReplyMarkup?
    /**
     * Inline message case
     */
    @TelegramMethod(
        methodName = "stopMessageLiveLocation"
    )
    @TelegramCodegen(
        methodName = "stopMessageLiveLocation",
        generatePseudoConstructor = true,
        pseudoConstructorName = "StopMessageLiveLocation"
    )
    data class InlineMessage internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val inlineMessageId: String,
        override val replyMarkup: ReplyMarkup? = null
    ) : JsonTelegramCallable<Boolean>(), StopMessageLiveLocation

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "stopMessageLiveLocation"
    )
    @TelegramCodegen(
        methodName = "stopMessageLiveLocation",
        generatePseudoConstructor = true,
        pseudoConstructorName = "StopMessageLiveLocation"
    )
    data class ChatMessage internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val chatId: ChatId,
        val messageId: Long,
        override val replyMarkup: ReplyMarkup? = null
    ) : JsonTelegramCallable<Boolean>(), StopMessageLiveLocation, HasChatId


}