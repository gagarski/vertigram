package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMethod


/**
 * Telegram [answerPreCheckoutQuery](https://core.telegram.org/bots/api#answerprecheckoutquery) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: OK and error.
 *
 * For up-to-date documentation please consult the official Telegram docs.
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
     * Case when ok is true
     */
    @TelegramMethod(
        methodName = "answerPreCheckoutQuery"
    )
    @TelegramCodegen.Method(
        name = "answerPreCheckoutQuery",
    )
    data class Ok internal constructor(
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
    @TelegramCodegen.Method(
        name = "answerPreCheckoutQuery",
    )
    data class Error internal constructor(
        override val preCheckoutQueryId: String,
        val errorMessage: String
    ) : AnswerPreCheckoutQuery() {
        val ok: Boolean = false
    }
}