package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Update

/**
 * Once the user has confirmed their payment and shipping details, the Bot API sends the final confirmation as an
 * [Update.PreCheckoutQuery]. Use
 * [ski.gagar.vertigram.telegram.methods.answerPreCheckoutQuery] to respond to such pre-checkout queries. Returns
 * `true` on success.
 *
 * The Bot API must receive an answer within 10 seconds after the pre-checkout query was sent.
 * [AnswerPreCheckoutQuery.Ok] and [AnswerPreCheckoutQuery.Error] represent the mutually exclusive values of
 * Telegram's `ok` parameter.
 *
 * See the official Telegram Bot API documentation for
 * [answerPreCheckoutQuery](https://core.telegram.org/bots/api#answerprecheckoutquery).
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "ok", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = AnswerPreCheckoutQuery.Ok::class, name = "true"),
    JsonSubTypes.Type(value = AnswerPreCheckoutQuery.Error::class, name = "false")
)
@TelegramCodegen.Method
sealed class AnswerPreCheckoutQuery : JsonTelegramCallable<Boolean>() {
    abstract val preCheckoutQueryId: String

    /**
     * Case when everything is alright and the bot is ready to proceed with the order.
     */
    @TelegramCodegen.Method(
        name = "answerPreCheckoutQuery",
    )
    data class Ok internal constructor(
        /** Unique identifier for the query to be answered. */
        override val preCheckoutQueryId: String
    ) : AnswerPreCheckoutQuery() {
        val ok: Boolean = true
    }

    /**
     * Case when there is a problem preventing the bot from proceeding with the order.
     */
    @TelegramCodegen.Method(
        name = "answerPreCheckoutQuery",
    )
    data class Error internal constructor(
        /** Unique identifier for the query to be answered. */
        override val preCheckoutQueryId: String,
        /**
         * Error message in human-readable form that explains the reason for failure to proceed with the checkout.
         * Telegram will display this message to the user.
         */
        val errorMessage: String
    ) : AnswerPreCheckoutQuery() {
        val ok: Boolean = false
    }
}
