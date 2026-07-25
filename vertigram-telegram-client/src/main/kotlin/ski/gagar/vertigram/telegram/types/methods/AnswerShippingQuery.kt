package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.ShippingOption
import ski.gagar.vertigram.telegram.types.Update

/**
 * If you sent an invoice requesting a shipping address and specified that the final price depends on the shipping
 * method, the Bot API will send an [Update.ShippingQuery] to the bot. Use this method to reply to shipping queries. On
 * success, `true` is returned.
 *
 * See Telegram's [answerShippingQuery](https://core.telegram.org/bots/api#answershippingquery) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "ok", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = AnswerShippingQuery.Ok::class, name = "true"),
    JsonSubTypes.Type(value = AnswerShippingQuery.Error::class, name = "false")
)
@TelegramCodegen.Method
sealed class AnswerShippingQuery : JsonTelegramCallable<Boolean>() {
    abstract val shippingQueryId: String
    /** Case when delivery to the specified address is possible. */
    @TelegramCodegen.Method(
        name = "answerShippingQuery"
    )
    data class Ok internal constructor(
        /** Unique identifier for the query to be answered. */
        override val shippingQueryId: String,
        /** List of available shipping options. */
        val shippingOptions: List<ShippingOption>
    ) : AnswerShippingQuery() {
        val ok: Boolean = true
    }

    /** Case when there is a problem preventing delivery to the specified address. */
    @TelegramCodegen.Method(
        name = "answerShippingQuery"
    )
    data class Error internal constructor(
        /** Unique identifier for the query to be answered. */
        override val shippingQueryId: String,
        /**
         * Error message in human-readable form that explains why it is impossible to complete the order. Telegram will
         * display this message to the user.
         */
        val errorMessage: String
    ) : AnswerShippingQuery() {
        val ok: Boolean = false
    }
}
