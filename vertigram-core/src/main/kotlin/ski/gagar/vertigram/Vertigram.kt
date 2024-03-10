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
import ski.gagar.vertigram.eventbus.messages.Reply
import ski.gagar.vertigram.eventbus.messages.Request
import ski.gagar.vertigram.eventbus.messages.replyWithSuccess
import ski.gagar.vertigram.eventbus.messages.replyWithThrowable
import ski.gagar.vertigram.services.VertigramInitializer
import ski.gagar.vertigram.util.coroMdcWith
import ski.gagar.vertigram.util.exceptions.VertigramException
import ski.gagar.vertigram.util.exceptions.VertigramInternalException
import ski.gagar.vertigram.util.jackson.mapTo
import ski.gagar.vertigram.util.jackson.toJsonObject
import ski.gagar.vertigram.util.jackson.typeReference
import ski.gagar.vertigram.verticles.common.VertigramVerticle
import java.util.*


private const val VERTIGRAMS = "ski.gagar.vertigram.vertigrams"

/**
 * Default [ObjectMapper] used in [Vertigram].
 *
 * Created as a copy of [DatabindCodec.mapper] with [KotlinModule] and [JavaTimeModule]
 */
fun defaultVertigramMapper(): ObjectMapper =
    DatabindCodec.mapper().copy()
        .registerModule(KotlinModule.Builder().build())
        .registerModule(JavaTimeModule())


/**
 * Root Vertigram object.
 *
 * Vertigram is a thin wrapper around [Vertx] node,
 * introducing object mapping protocol and namespaces on top of [io.vertx.core.eventbus.EventBus].
 *
 * Vertigram can be atached to [Vertx] instance by calling [attachVertigram] method,
 * returning [Vertigram] instance. [Vertx] can have multiple [Vertigram]s attached, each of them
 * should have unique name, defined in [config].
 *
 * On top of [Vertx] [Vertigram] introduces:
 *  - Jackson-based protocol on [Vertx.eventBus]
 *  - [VertigramVerticle] which can typed configuration (see [deployVerticle])
 *  - namespaces for event bus addresses
 *
 *  The most convenient way to interact with [VertigramVerticle]s and [Vertigram.EventBus] is by using
 *  [Vertigram.EventBus] API, however, you can interac with it using plain [Vertx], given that you follow
 *  the JSON serialization protocol.
 *
 *  Vertigram is purely local. If you want to use it in clustered environment, you need to attach a
 *  compatible [Vertigram] instance attached to each node.
 */
class Vertigram(
    /**
     * [Vertx] instance
     */
    val vertx: Vertx,
    /**
     * Configuration
     */
    val config: Config
) : Shareable {
    /**
     * Vertigram name
     */
    val name: String = config.name

    /**
     * Object mapper
     */
    val objectMapper: ObjectMapper = config.objectMapper

    /**
     * Vertigram event bus
     */
    val eventBus: EventBus = EventBus()

    /**
     * Create [Vertigram.DeploymentOptions] from config
     */
    fun <T> deploymentOptions(config: T) = DeploymentOptions(this, config)

    /**
     * Deploy [verticle] using [deploymentOptions]
     *
     * @return deployment id
     */
    suspend fun <T> deployVerticle(verticle: VertigramVerticle<T>, deploymentOptions: DeploymentOptions<T>) =
        vertx.deployVerticle(verticle, deploymentOptions).coAwait()

    /**
     * Deploy [verticle] using typed [config]
     *
     * @return deployment id
     */
    suspend fun <T> deployVerticle(verticle: VertigramVerticle<T>, config: T) =
        vertx.deployVerticle(verticle, DeploymentOptions(this, config)).coAwait()

    /**
     * Deploy verticle without configuration ([Unit]? config type)
     *
     * @return deployment id
     */
    suspend fun deployVerticle(verticle: VertigramVerticle<Unit?>) =
        vertx.deployVerticle(verticle, DeploymentOptions(this)).coAwait()

    /**
     * Undeploy [VertigramVerticle] by [deploymentId]
     */
    suspend fun undeploy(deploymentId: String) = vertx.undeploy(deploymentId).coAwait()

    @PublishedApi
    @JvmInline
    internal value class VertigramAddress(val address: String)

    @PublishedApi
    internal fun vertigramAddress(address: String) = VertigramAddress("vertigram:$name://$address")

    /**
     * Vertigram event bus wrapper around [io.vertx.core.eventbus.EventBus].
     *
     * Provides wrappers for typical [io.vertx.core.eventbus.EventBus] operations, applying
     * Vertigram protocol.
     *
     * It relies on original Vertx [io.vertx.core.eventbus.EventBus] to be able to serialize
     * [JsonObject] using default codecs (default behavior).
     *
     * Addresses for each operation are converted from Vertigram address to plain Vertx address using following notation:
     * `com.example.address` Vertigram address becomes `vertigram:default://com.example.address` for `default` Vertigram instance.
     *
     * For some operations transferred objects are wrapped into [Request] or [Reply] types, see documentation for methods
     * for more details
     */
    inner class EventBus {
        /**
         * Raw [io.vertx.core.eventbus.EventBus]
         */
        val raw = vertx.eventBus()

        /**
         * Object mapper passed from [Config.objectMapper]
         */
        val objectMapper: ObjectMapper
            get() = this@Vertigram.objectMapper

        /**
         * Publish a [value] to Vertigram [address] with [options].
         *
         * Published value is wrapped into [Request] with [value] as [Request.payload] and serialized using [objectMapper].
         *
         * @param address Vertigram address
         * @param value Value to publish
         * @param options Delivery options
         *
         * @see io.vertx.core.eventbus.EventBus.send
         */
        fun <RequestPayload> publish(
            address: String,
            value: RequestPayload,
            options: DeliveryOptions = DeliveryOptions()
        ) {
            raw.publish(vertigramAddress(address).address, Request(value).toJsonObject(objectMapper), options)
        }

        /**
         * Send a [value] to Vertigram [address] with [options].
         *
         * Sent value is wrapped into [Request] with [value] as [Request.payload] and serialized using [objectMapper].
         *
         * @param address Vertigram address
         * @param value Value to send
         * @param options Delivery options
         *
         * @see io.vertx.core.eventbus.EventBus.send
         */
        fun <RequestPayload> send(
            address: String,
            value: RequestPayload,
            options: DeliveryOptions = DeliveryOptions()
        ): io.vertx.core.eventbus.EventBus =
            raw.send(vertigramAddress(address).address, Request(value).toJsonObject(objectMapper), options)

        /**
         * Non-reified version of [consumer]
         *
         * @param requestJavaType [JavaType] for request
         *
         * @see consumer
         * @see VertigramVerticle.consumer
         * @see io.vertx.core.eventbus.EventBus.consumer
         */
        inline fun <RequestPayload, Result> consumerNonReified(
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

        /**
         * Non-reified version of [localConsumer]
         *
         * @param requestJavaType [JavaType] for request
         *
         * @see localConsumer
         * @see VertigramVerticle.localConsumer
         * @see io.vertx.core.eventbus.EventBus.localConsumer
         */
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

        /**
         * Attach a consumer [function] to event bus on Vertigram [address].
         *
         * A value passed to [function] as a single argument is expected to be wrapped to [Request] as a [Request.payload]
         * on raw event bus and deserialized by Vertigram using [objectMapper]
         *
         * If there is a return value it's sent to [request]or wrapped in [Reply] as [Reply.Success.payload].
         *
         * If an exception is thrown the following happens based on the type of the exception:
         *  - If an exception is an instance of [VertigramException] it is being passed as is as [Reply.Error.error]
         *    inside [Reply] object
         *  - Other exceptions are returned in [Reply.Error] as [VertigramInternalException]
         *  - [VertigramInternalException]s themselves are being replaced with default [VertigramInternalException]
         *    with default message, unless [Config.hideInternalExceptions] is false. In that case they are passed
         *    as is.
         *  - If [Config.hideInternalExceptions] is false then exceptions outside [VertigramException] hierarchy are transformed
         *    into [VertigramInternalException] with the same message.
         *
         * @param coroScope Coroutine scope
         * @param address Vertigram address
         * @param replyOptions Reply options
         * @param requestJavaType Request java type
         * @param function Consumer itself
         *
         * @see VertigramVerticle.consumer
         * @see io.vertx.core.eventbus.EventBus.consumer
         */
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

        /**
         * Attach a local consumer [function] to event bus on Vertigram [address].
         *
         * The same protocol is used as for [consumer].
         *
         * @param coroScope Coroutine scope
         * @param address Vertigram address
         * @param replyOptions Reply options
         * @param requestJavaType Request java type
         * @param function Consumer itself
         *
         * @see VertigramVerticle.localConsumer
         * @see io.vertx.core.eventbus.EventBus.localConsumer
         */
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

        /**
         * Send a request to [consumer] on Vertigram [address].
         *
         * See [consumer] for details on protocols for the passed values, return values and exceptions.
         *
         * @see VertigramVerticle.consumer
         * @see io.vertx.core.eventbus.EventBus.request
         */
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

    /**
     * Vertigram configuration
     */
    class Config(
        /**
         * Name
         */
        val name: String = DEFAULT_NAME,
        /**
         * Used object mapper
         */
        val objectMapper: ObjectMapper = defaultVertigramMapper(),
        /**
         * Should exceptions transformed to [VertigramInternalException]s or [VertigramInternalException]s themselves
         * be replaced with default [VertigramInternalException]
         */
        val hideInternalExceptions: Boolean = true,
        /**
         * A list of initializers that are being called by [attachVertigram].
         *
         * If null, then initializers discovered by [ServiceLoader] will be called.
         */
        val initializers: List<VertigramInitializer>? = null
    ) {
        companion object {
            /**
             * Default Vertigram name
             */
            const val DEFAULT_NAME = "default"
        }
    }

    /**
     * A wrapper for [io.vertx.core.DeploymentOptions] with typed [Config].
     *
     * The [config] is a constructor argument, making the configuration mandatory.
     * [config] is wrapped with [VertigramVerticle.ConfigWrapper] object,
     * which contains Vertigram name and the config itself.
     */
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

    /**
     * Convenience function to create [DeploymentOptions] for non-configured verticles
     */
    fun DeploymentOptions(
        vertigram: Vertigram
    ) = DeploymentOptions(vertigram, null)

    companion object {
        const val CONSUMER_ADDRESS_MDC = "consumerAddress"
    }
}

/**
 * Attach [Vertigram] with given [config] to [this]
 */
fun Vertx.attachVertigram(config: Vertigram.Config = Vertigram.Config()): Vertigram =
    sharedData().getLocalMap<String, Vertigram>(VERTIGRAMS)
        .compute(config.name) { _, old ->
            if (old != null)
                throw IllegalStateException("Vertigram ${config.name} is already attached")

            Vertigram(this, config).apply {
                val initializers = config.initializers ?: ServiceLoader.load(VertigramInitializer::class.java)
                for (it in initializers) {
                    with (it) {
                        initialize()
                    }
                }
            }

        }!!.also {
            logUnhandledExceptions()
        }

/**
 * Detach [Vertigram] by [name] from [this]
 */
fun Vertx.detachVertigram(name: String = Vertigram.Config.DEFAULT_NAME) =
    sharedData().getLocalMap<String, Vertigram>(VERTIGRAMS).remove(name)

/**
 * Obtain previously attached [Vertigram] by [name] from [this]
 */
fun Vertx.getVertigram(name: String = Vertigram.Config.DEFAULT_NAME) =
    sharedData().getLocalMap<String, Vertigram>(VERTIGRAMS)[name]
        ?: throw IllegalArgumentException("Vertigram $name is not attached to this Ver.X instance")