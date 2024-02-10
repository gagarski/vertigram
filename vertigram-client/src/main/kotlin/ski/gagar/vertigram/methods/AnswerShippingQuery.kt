package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.ShippingOption
import ski.gagar.vertigram.util.NoPosArgs
import ski.gagar.vertigram.util.TgMethodName
import ski.gagar.vertigram.util.TgVerticleGenerate

/**
 * Telegram [answerShippingQuery](https://core.telegram.org/bots/api#answershippingquery) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed class AnswerShippingQuery : JsonTgCallable<Boolean>() {
    /**
     * Case when ok is true
     */
    @TgMethod
    @TgMethodName("answerShippingQuery")
    @TgVerticleGenerate(address = "answerShippingQueryOk")
    @TelegramCodegen(
        methodName = "answerShippingQuery",
        generatePseudoConstructor = true,
        pseudoConstructorName = "AnswerShippingQuery"
    )
    data class Ok internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val shippingQueryId: String,
        val shippingOptions: List<ShippingOption>
    ) : AnswerShippingQuery() {
        val ok: Boolean = true
    }

    /**
     * Case when ok is false
     */
    @TgMethod
    @TgMethodName("answerShippingQuery")
    @TgVerticleGenerate(address = "answerShippingQueryError")
    @TelegramCodegen(
        methodName = "answerShippingQuery",
        generatePseudoConstructor = true,
        pseudoConstructorName = "AnswerShippingQuery"
    )
    data class Error internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val shippingQueryId: String,
        val errorMessage: String
    ) : AnswerShippingQuery() {
        val ok: Boolean = false
    }
}