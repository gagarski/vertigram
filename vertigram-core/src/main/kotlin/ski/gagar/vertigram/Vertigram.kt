package ski.gagar.vertigram

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.Vertx
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.eventbus.Message
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.json.JsonObject
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.core.shareddata.Shareable
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.slf4j.MDCContext
import ski.gagar.vertigram.eventbus.exceptions.VertigramException
import ski.gagar.vertigram.eventbus.exceptions.VertigramInternalException
import ski.gagar.vertigram.eventbus.messages.Reply
import ski.gagar.vertigram.eventbus.messages.Request
import ski.gagar.vertigram.eventbus.messages.replyWithSuccess
import ski.gagar.vertigram.eventbus.messages.replyWithThrowable
import ski.gagar.vertigram.jackson.mapTo
import ski.gagar.vertigram.jackson.toJsonObject
import ski.gagar.vertigram.jackson.typeReference
import ski.gagar.vertigram.services.VertigramInitializer
import ski.gagar.vertigram.util.coroMdcWith
import ski.gagar.vertigram.verticles.common.VertigramVerticle
import java.util.*


const val VERTIGRAMS = "ski.gagar.vertigram.vertigrams"

fun defaultVertigramMapper() =
    DatabindCodec.mapper().copy()
        .registerModule(KotlinModule.Builder().build())
        .registerModule(JavaTimeModule())



class Vertigram(
    val vertx: Vertx,
    val config: Config
) : Shareable {
    val name: String = config.name
    val objectMapper: ObjectMapper = config.objectMapper
    val eventBus: EventBus = EventBus()

    fun <T> deploymentOptions(config: T) = DeploymentOptions(this, config)

    suspend fun <T> deployVerticle(verticle: VertigramVerticle<T>, deploymentOptions: DeploymentOptions<T>) =
        vertx.deployVerticle(verticle, deploymentOptions).coAwait()

    suspend fun deployVerticle(verticle: VertigramVerticle<Unit?>) =
        vertx.deployVerticle(verticle, DeploymentOptions(this)).coAwait()

    @PublishedApi
    @JvmInline
    internal value class VertigramAddress(val address: String)

    @PublishedApi
    internal fun vertigramAddress(address: String) = VertigramAddress("vertigram:$name://$address")

    inner class EventBus {
        val raw = vertx.eventBus()
        val objectMapper: ObjectMapper
            get() = this@Vertigram.objectMapper

        fun <RequestPayload> publish(address: String,
                                     value: RequestPayload,
                                     options: DeliveryOptions = DeliveryOptions()
        ): io.vertx.core.eventbus.EventBus =
            raw.publish(vertigramAddress(address).address, Request(value).toJsonObject(objectMapper), options)

        fun <RequestPayload> send(address: String,
                                  value: RequestPayload,
                                  options: DeliveryOptions = DeliveryOptions()
        ): io.vertx.core.eventbus.EventBus =
            raw.send(vertigramAddress(address).address, Request(value).toJsonObject(objectMapper), options)

        @PublishedApi
        internal inline fun <RequestPayload, Result> consumerNonReified(
            coroScope: CoroutineScope,
            address: String,
            replyOptions: DeliveryOptions = DeliveryOptions(),
            requestJavaType: JavaType,
            crossinline function: suspend (RequestPayload) -> Result
        ) : MessageConsumer<JsonObject> {
            val reqWrapperType = objectMapper.typeFactory.constructParametricType(
                Request::class.java,
                requestJavaType
            )
            return raw.consumer(vertigramAddress(address).address) { msg: Message<JsonObject> ->
                coroScope.launch(MDCContext(coroScope.coroMdcWith(CONSUMER_ADDRESS_MDC to vertigramAddress(address).address))) {
                    val reqW: Request<RequestPayload> = msg.body()!!.mapTo(reqWrapperType, objectMapper)

                    try {
                        msg.replyWithSuccess(function(reqW.payload), this@Vertigram, replyOptions)
                    } catch (t: Throwable) {
                        msg.replyWithThrowable<RequestPayload>(t, this@Vertigram, replyOptions)
                        when (t) {
                            is VertigramInternalException -> throw t
                            is VertigramException -> {}
                            else -> throw t
                        }
                    }
                }
            }
        }

        @PublishedApi
        internal inline fun <RequestPayload, Result> localConsumerNonReified(
            coroScope: CoroutineScope,
            address: String,
            replyOptions: DeliveryOptions = DeliveryOptions(),
            requestJavaType: JavaType,
            crossinline function: suspend (RequestPayload) -> Result
        ) : MessageConsumer<JsonObject> {
            val reqWrapperType = objectMapper.typeFactory.constructParametricType(
                Request::class.java,
                requestJavaType
            )
            return raw.localConsumer(vertigramAddress(address).address) { msg: Message<JsonObject> ->
                coroScope.launch(MDCContext(coroScope.coroMdcWith(CONSUMER_ADDRESS_MDC to vertigramAddress(address).address))) {
                    val reqW: Request<RequestPayload> = msg.body()!!.mapTo(reqWrapperType, objectMapper)

                    try {
                        msg.replyWithSuccess(function(reqW.payload), this@Vertigram, replyOptions)
                    } catch (t: Throwable) {
                        msg.replyWithThrowable<RequestPayload>(t, this@Vertigram, replyOptions)
                        when (t) {
                            is VertigramInternalException -> throw t
                            is VertigramException -> {}
                            else -> throw t
                        }
                    }
                }
            }
        }

        inline fun <reified RequestPayload, Result> consumer(
            coroScope: CoroutineScope,
            address: String,
            replyOptions: DeliveryOptions = DeliveryOptions(),
            requestJavaType: JavaType = objectMapper.typeFactory.constructType(typeReference<RequestPayload>().type),
            crossinline function: suspend (RequestPayload) -> Result
        ) = consumerNonReified(
            coroScope = coroScope,
            address = address,
            replyOptions = replyOptions,
            requestJavaType = requestJavaType,
            function = function
        )

        inline fun <reified RequestPayload, Result> localConsumer(
            coroScope: CoroutineScope,
            address: String,
            replyOptions: DeliveryOptions = DeliveryOptions(),
            requestJavaType: JavaType = objectMapper.typeFactory.constructType(typeReference<RequestPayload>().type),
            crossinline function: suspend (RequestPayload) -> Result
        ) = localConsumerNonReified(
            coroScope = coroScope,
            address = address,
            replyOptions = replyOptions,
            requestJavaType = requestJavaType,
            function = function
        )

        suspend inline fun <RequestPayload, reified Result> request(
            address: String,
            value: RequestPayload,
            resultJavaType: JavaType = objectMapper.typeFactory.constructType(typeReference<Result>().type),
            options: DeliveryOptions = DeliveryOptions()
        ): Result {
            val replyType = objectMapper.typeFactory.constructParametricType(
                Reply::class.java,
                resultJavaType
            )
            val reply: Reply<Result> =
                raw.request<JsonObject>(
                    vertigramAddress(address).address,
                    JsonObject.mapFrom(Request(value)),
                    options
                )
                    .coAwait()
                    .body()
                    .mapTo(replyType, objectMapper)

            when (reply) {
                is Reply.Error<*> -> reply.doThrow()
                is Reply.Success<*> -> return reply.payload as Result
            }
        }
    }

    class Config(
        val name: String = DEFAULT_NAME,
        val objectMapper: ObjectMapper = defaultVertigramMapper(),
        val hideInternalExceptions: Boolean = true
    ) {
        companion object {
            const val DEFAULT_NAME = "default"
        }
    }

    class DeploymentOptions<Config>(
        vertigram: Vertigram,
        config: Config
    ): io.vertx.core.DeploymentOptions() {
        init {
            @Suppress("DEPRECATION")
            setConfig(VertigramVerticle.ConfigWrapper(vertigram.name, config).toJsonObject(vertigram.objectMapper))
        }

        @Deprecated(message = "Use constructor argument instead")
        override fun getConfig(): JsonObject {
            return super.getConfig()
        }

        @Deprecated(message = "Use constructor argument instead")
        override fun setConfig(config: JsonObject?): io.vertx.core.DeploymentOptions {
            return super.setConfig(config)
        }
    }

    fun DeploymentOptions(
        vertigram: Vertigram
    ) = DeploymentOptions(vertigram, null)

    companion object {
        const val CONSUMER_ADDRESS_MDC = "consumerAddress"
    }
}

fun Vertx.attachVertigram(config: Vertigram.Config = Vertigram.Config()): Vertigram =
    sharedData().getLocalMap<String, Vertigram>(VERTIGRAMS)
        .compute(config.name) { _, old ->
            if (old != null)
                throw IllegalStateException("Vertigram ${config.name} is already attached")

            Vertigram(this, config).apply {
                ServiceLoader.load(VertigramInitializer::class.java).forEach {
                    with (it) {
                        initialize()
                    }
                }
            }

        }!!

fun Vertx.detachVertigram(name: String = Vertigram.Config.DEFAULT_NAME) =
    sharedData().getLocalMap<String, Vertigram>(VERTIGRAMS).remove(name)

fun Vertx.getVertigram(name: String = Vertigram.Config.DEFAULT_NAME) =
    sharedData().getLocalMap<String, Vertigram>(VERTIGRAMS)[name]
        ?: throw IllegalArgumentException("Vertigram $name is not attached to this Ver.X instance")