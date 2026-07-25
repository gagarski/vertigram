package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Use this method to process a received chat join request query. Returns `true` on success.
 *
 * See Telegram's
 * [answerChatJoinRequestQuery](https://core.telegram.org/bots/api#answerchatjoinrequestquery) documentation.
 */
@TelegramCodegen.Method
data class AnswerChatJoinRequestQuery internal constructor(
    /** Unique identifier of the join request query. */
    val chatJoinRequestQueryId: String,
    /** Result of the query. */
    val result: Result
) : JsonTelegramCallable<Boolean>() {
    enum class Result {
        /** Allows the user to join the chat. */
        @JsonProperty(APPROVE_STR)
        APPROVE,
        /** Disallows the user from joining the chat. */
        @JsonProperty(DECLINE_STR)
        DECLINE,
        /** Leaves the decision to other administrators. */
        @JsonProperty(QUEUE_STR)
        QUEUE;

        companion object {
            const val APPROVE_STR = "approve"
            const val DECLINE_STR = "decline"
            const val QUEUE_STR = "queue"
        }
    }
}
