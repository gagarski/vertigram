package ski.gagar.vxutil

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import io.vertx.core.Verticle
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.impl.NoStackTraceThrowable
import io.vertx.core.json.JsonObject
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.launch

interface DoNotSuppressError

class ReplyException(msg: String?, override val cause: Throwable) : Exception(msg, cause)
class InternalServerError(msg: String) : NoStackTraceThrowable(msg)

@PublishedApi
internal data class Reply<T>(
    val result: T?,

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    val error: Throwable? = null
) {
    @PublishedApi
    internal fun doThrow() {
        if (error != null)
            throw ReplyException("The consumer responded with an error", error)

    }

    val success: Boolean
        @JsonIgnore
        get() = error == null

    companion object {
        fun error(error: Throwable) = Reply(null, error)
        fun <T> success(result: T) = Reply(result)
    }
}

val DEBUG by lazy {
    System.getProperty("ski.gagar.vxutil.debug", "false") == "true"
}

val TYPE_FACTORY: TypeFactory = DatabindCodec.mapper().typeFactory

suspend inline fun <Argument, reified Result> EventBus.requestJsonAwait(
    address: String,
    value: Argument,
    resultJavaType: JavaType = TYPE_FACTORY.constructType(Result::class.java),
    options: DeliveryOptions = DeliveryOptions()
): Result {
    val reply: Reply<Result> =
        request<JsonObject>(address, JsonObject.mapFrom(value), options).await()
            .body()
            .mapTo(TYPE_FACTORY.constructParametricType(Reply::class.java, resultJavaType))

    if (!reply.success)
        reply.doThrow()

    return reply.result as Result
}


fun <Request> EventBus.publishJson(address: String,
                                   value: Request,
                                   options: DeliveryOptions = DeliveryOptions()): EventBus =
    publish(address, JsonObject.mapFrom(value), options)

fun Message<JsonObject?>.replyWithThrowable(t: Throwable, options: DeliveryOptions = DeliveryOptions()) =
    reply(
        when {
            DEBUG -> JsonObject.mapFrom(
                Reply.error(
                    t
                )
            )
            t is DoNotSuppressError -> {
                t.stackTrace = emptyArray()
                JsonObject.mapFrom(Reply.error(t))
            }
            else -> JsonObject.mapFrom(
                Reply.error(
                    InternalServerError(
                        "Something went wrong"
                    )
                )
            )
        },
        options
    )

@PublishedApi
internal inline fun <reified Result> Message<JsonObject?>.replyWithSuccess(
    res: Result,
    options: DeliveryOptions = DeliveryOptions()
) =
    if (Result::class.java == Unit::class.java)
        reply(JsonObject.mapFrom(Reply.success(null)), options)
    else
        reply(JsonObject.mapFrom(Reply.success(res)), options)

inline fun <reified Request, reified Result> Verticle.jsonConsumer(
    address: String,
    replyOptions: DeliveryOptions = DeliveryOptions(),
    requestJavaType: JavaType = TYPE_FACTORY.constructType(Request::class.java),
    crossinline function: (Request) -> Result
) : MessageConsumer<JsonObject> = vertx.eventBus().consumer(address) { msg ->
    val req: Request =
        msg.body().mapTo(requestJavaType)
    val res = try {
        function(req)
    } catch (t: Throwable) {
        msg.replyWithThrowable(t, replyOptions)
        throw t
    }
    msg.replyWithSuccess(res, replyOptions)
}

inline fun <reified Request, reified Result>
        CoroutineVerticle.suspendJsonConsumer(
    address: String,
    replyOptions: DeliveryOptions = DeliveryOptions(),
    requestJavaType: JavaType = TYPE_FACTORY.constructType(Request::class.java),
    crossinline function: suspend (Request) -> Result
) : MessageConsumer<JsonObject> = vertx.eventBus().consumer(address) { msg ->
    launch {
        val req: Request =
            msg.body().mapTo(requestJavaType)
        val res = try {
            function(req)
        } catch (t: Throwable) {
            msg.replyWithThrowable(t, replyOptions)
            throw t
        }
        msg.replyWithSuccess(res, replyOptions)
    }
}
