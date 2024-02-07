package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.annotations.TgSuperClass
import ski.gagar.vertigram.util.NoPosArgs
import ski.gagar.vertigram.util.TgMethodName
import ski.gagar.vertigram.util.TgVerticleGenerate


/**
 * Telegram [answerPreCheckoutQuery](https://core.telegram.org/bots/api#answerprecheckoutquery) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TgSuperClass
sealed class AnswerPreCheckoutQuery : JsonTgCallable<Boolean>() {
    /**
     * Case when ok is true
     */
    @TgMethod(kotlinMethodName = "answerPreCheckoutQuery")
    @TgMethodName("answerPreCheckoutQuery")
    @TgVerticleGenerate(address = "answerPreCheckoutQueryOk")
    data class Ok(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val preCheckoutQueryId: String
    ) : AnswerPreCheckoutQuery() {
        val ok: Boolean = true
    }

    /**
     * Case when ok is false
     */
    @TgMethod(kotlinMethodName = "answerPreCheckoutQuery")
    @TgMethodName("answerPreCheckoutQuery")
    @TgVerticleGenerate(address = "answerPreCheckoutQueryError")
    data class Error(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val preCheckoutQueryId: String,
        val errorMessage: String
    ) : AnswerPreCheckoutQuery() {
        val ok: Boolean = false
    }
}