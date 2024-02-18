package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.types.ShippingOption
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [answerShippingQuery](https://core.telegram.org/bots/api#answershippingquery) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: OK and error.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed class AnswerShippingQuery : JsonTelegramCallable<Boolean>() {
    abstract val shippingQueryId: String
    /**
     * Case when ok is true
     */
    @TelegramMethod(
        methodName = "answerShippingQuery"
    )
    @TelegramCodegen(
        methodName = "answerShippingQuery",
        generatePseudoConstructor = true,
        pseudoConstructorName = "AnswerShippingQuery"
    )
    data class Ok internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val shippingQueryId: String,
        val shippingOptions: List<ShippingOption>
    ) : AnswerShippingQuery() {
        val ok: Boolean = true
    }

    /**
     * Case when ok is false
     */
    @TelegramMethod(
        methodName = "answerShippingQuery"
    )
    @TelegramCodegen(
        methodName = "answerShippingQuery",
        generatePseudoConstructor = true,
        pseudoConstructorName = "AnswerShippingQuery"
    )
    data class Error internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val shippingQueryId: String,
        val errorMessage: String
    ) : AnswerShippingQuery() {
        val ok: Boolean = false
    }
}