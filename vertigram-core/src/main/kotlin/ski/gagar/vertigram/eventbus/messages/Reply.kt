package ski.gagar.vertigram.eventbus.messages

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.eventbus.exceptions.VertigramException
import ski.gagar.vertigram.eventbus.exceptions.VertigramInternalException
import ski.gagar.vertigram.jackson.toJsonObject

/**
 * Reply wrapper for [Vertigram.EventBus]
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(Reply.Success::class),
    JsonSubTypes.Type(Reply.Error::class)
)
sealed interface Reply<T> {
    data class Success<T>(val payload: T) : Reply<T>
    data class Error<T>(val error: VertigramException) : Reply<T> {
        fun doThrow(): Nothing {
            error.fillInStackTrace()
            throw error
        }
    }

    companion object {
        fun <T> internalError() = Error<T>(VertigramException("Internal error"))
        fun <T> internalError(msg: String?) = Error<T>(VertigramException(msg ?: "Internal error"))
    }
}

@PublishedApi
internal fun <Result> Message<JsonObject>.replyWithSuccess(
    res: Result,
    vertigram: Vertigram,
    options: DeliveryOptions = DeliveryOptions()
) {
    when {
        null == replyAddress() -> {}
        else -> reply(Reply.Success(res).toJsonObject(vertigram.objectMapper), options)
    }
}

@PublishedApi
internal fun <Result> Message<JsonObject>.replyWithThrowable(
    t: Throwable,
    vertigram: Vertigram,
    options: DeliveryOptions = DeliveryOptions()
) {
    if (null == replyAddress()) return

    val response: Reply<Result> = when {
        t is VertigramInternalException && vertigram.config.hideInternalExceptions ->
            Reply.internalError()
        t is VertigramException -> Reply.Error(t)
        vertigram.config.hideInternalExceptions -> Reply.internalError()
        else -> Reply.internalError(t.message)
    }

    val stackTrace = t.stackTrace
    t.stackTrace = arrayOf()

    val jo = response.toJsonObject(vertigram.objectMapper)

    t.stackTrace = stackTrace

    reply(jo, options)
}