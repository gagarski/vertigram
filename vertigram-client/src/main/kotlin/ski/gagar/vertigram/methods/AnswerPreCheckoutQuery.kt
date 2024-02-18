package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.util.NoPosArgs


/**
 * Telegram [answerPreCheckoutQuery](https://core.telegram.org/bots/api#answerprecheckoutquery) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: OK and error.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed class AnswerPreCheckoutQuery : JsonTelegramCallable<Boolean>() {
    abstract val preCheckoutQueryId: String

    /**
     * Case when ok is true
     */
    @TelegramMethod(
        methodName = "answerPreCheckoutQuery"
    )
    @TelegramCodegen(
        methodName = "answerPreCheckoutQuery",
        generatePseudoConstructor = true,
        pseudoConstructorName = "AnswerPreCheckoutQuery"
    )
    data class Ok internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val preCheckoutQueryId: String
    ) : AnswerPreCheckoutQuery() {
        val ok: Boolean = true
    }

    /**
     * Case when ok is false
     */
    @TelegramMethod(
        methodName = "answerPreCheckoutQuery"
    )
    @TelegramCodegen(
        methodName = "answerPreCheckoutQuery",
        generatePseudoConstructor = true,
        pseudoConstructorName = "AnswerPreCheckoutQuery"
    )
    data class Error internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val preCheckoutQueryId: String,
        val errorMessage: String
    ) : AnswerPreCheckoutQuery() {
        val ok: Boolean = false
    }
}