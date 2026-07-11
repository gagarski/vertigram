package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [answerChatJoinRequestQuery](https://core.telegram.org/bots/api#answerchatjoinrequestquery) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class AnswerChatJoinRequestQuery internal constructor(
    val chatJoinRequestQueryId: String,
    val result: Result
) : JsonTelegramCallable<Boolean>() {
    enum class Result {
        @JsonProperty(APPROVE_STR)
        APPROVE,
        @JsonProperty(DECLINE_STR)
        DECLINE,
        @JsonProperty(QUEUE_STR)
        QUEUE;

        companion object {
            const val APPROVE_STR = "approve"
            const val DECLINE_STR = "decline"
            const val QUEUE_STR = "queue"
        }
    }
}
