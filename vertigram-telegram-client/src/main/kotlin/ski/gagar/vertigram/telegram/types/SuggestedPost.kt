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
     * Telegram [SuggestedPostPrice](https://core.telegram.org/bots/api#suggestedpostprice) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Price internal constructor(
        val currency: String,
        val amount: Long
    ) {
        companion object
    }

    /**
     * Telegram [SuggestedPostInfo](https://core.telegram.org/bots/api#suggestedpostinfo) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Info internal constructor(
        val state: State,
        val price: Price? = null,
        val sendDate: Instant? = null
    ) {
        enum class State {
            @JsonProperty(PENDING_STR)
            PENDING,
            @JsonProperty(APPROVED_STR)
            APPROVED,
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
     * Telegram [SuggestedPostParameters](https://core.telegram.org/bots/api#suggestedpostparameters) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Parameters internal constructor(
        val price: Price? = null,
        val sendDate: Instant? = null
    ) {
        companion object
    }
}
