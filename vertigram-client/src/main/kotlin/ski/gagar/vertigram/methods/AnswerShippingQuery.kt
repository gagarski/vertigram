package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.annotations.TgSuperClass
import ski.gagar.vertigram.types.ShippingOption
import ski.gagar.vertigram.util.NoPosArgs
import ski.gagar.vertigram.util.TgMethodName
import ski.gagar.vertigram.util.TgVerticleGenerate

/**
 * Telegram [answerShippingQuery](https://core.telegram.org/bots/api#answershippingquery) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TgSuperClass
sealed class AnswerShippingQuery : JsonTgCallable<Boolean>() {
    /**
     * Case when ok is true
     */
    @TgMethod(kotlinMethodName = "answerShippingQuery")
    @TgMethodName("answerShippingQuery")
    @TgVerticleGenerate(address = "answerShippingQueryOk")
    data class Ok(
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
    @TgMethod(kotlinMethodName = "answerShippingQuery")
    @TgMethodName("answerShippingQuery")
    @TgVerticleGenerate(address = "answerShippingQueryError")
    data class Error(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val shippingQueryId: String,
        val errorMessage: String
    ) : AnswerShippingQuery() {
        val ok: Boolean = false
    }
}