package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [stopMessageLiveLocation](https://core.telegram.org/bots/api#stopmessagelivelocation) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed interface StopMessageLiveLocation {
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
        val replyMarkup: ReplyMarkup? = null
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
        val replyMarkup: ReplyMarkup? = null
    ) : JsonTelegramCallable<Boolean>(), StopMessageLiveLocation, HasChatId


}