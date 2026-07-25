package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Telegram types related to suggested posts.
 *
 * Nested classes correspond to Telegram types with the `SuggestedPost` prefix.
 */
object SuggestedPost {
    /**
     * Contains information about the price of a suggested post.
     *
     * See Telegram's [SuggestedPostPrice](https://core.telegram.org/bots/api#suggestedpostprice) documentation.
     */
    @TelegramCodegen.Type
    data class Price internal constructor(
        /** Currency in which the post will be paid. */
        val currency: String,
        /** Amount of the currency that will be paid for the post. */
        val amount: Long
    ) {
        companion object
    }

    /**
     * Contains information about a suggested post.
     *
     * See Telegram's [SuggestedPostInfo](https://core.telegram.org/bots/api#suggestedpostinfo) documentation.
     */
    @TelegramCodegen.Type
    data class Info internal constructor(
        /** State of the suggested post. */
        val state: State,
        /** Price of the suggested post. */
        val price: Price? = null,
        /** Proposed send date of the suggested post. */
        val sendDate: Instant? = null
    ) {
        enum class State {
            /** Suggested post is pending approval. */
            @JsonProperty(PENDING_STR)
            PENDING,
            /** Suggested post was approved. */
            @JsonProperty(APPROVED_STR)
            APPROVED,
            /** Suggested post was declined. */
            @JsonProperty(DECLINED_STR)
            DECLINED;

            companion object {
                const val PENDING_STR = "pending"
                const val APPROVED_STR = "approved"
                const val DECLINED_STR = "declined"
            }
        }

        companion object
    }

    /**
     * Contains parameters of a post suggested by the bot.
     *
     * See Telegram's [SuggestedPostParameters](https://core.telegram.org/bots/api#suggestedpostparameters)
     * documentation.
     */
    @TelegramCodegen.Type
    data class Parameters internal constructor(
        /** Proposed price for the post. */
        val price: Price? = null,
        /** Proposed send date of the post. */
        val sendDate: Instant? = null
    ) {
        companion object
    }
}
