package ski.gagar.vertigram.verticles.common

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.slf4j.MDCContext
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.coroutines.VerticleName
import ski.gagar.vertigram.getVertigram
import ski.gagar.vertigram.util.jackson.mapTo
import ski.gagar.vertigram.util.jackson.typeReference
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import kotlin.coroutines.CoroutineContext


/**
 * [Vertigram] verticle.
 *
 * Provides the following features on top of [CoroutineVerticle]:
 *  - [name] for verticle
 *  - access to [Vertigram] instance and therefore to [Vertigram.EventBus] instance
 *  - strongly-typed [typedConfig]
 *
 * For more details on deploying and interacting with it without [Vertigram],
 * see the docs for [Vertigram].
 *
 * @see [Vertigram]
 */
abstract class VertigramVerticle<Config> : CoroutineVerticle() {
    lateinit var vertigram: Vertigram
        private set

    /**
     * Should be overridden to store [TypeReference] to config
     *
     * In most cases it's enough to just call [typeReference]
     *
     * @see typeReference
     * @sample typeReferenceExample
     */
    protected abstract val configTypeReference: TypeReference<Config>

    private lateinit var context: Context

    /**
     * Verticle name. May be overriden by subclasses.
     */
    open val name: String by lazy {
        "${this.javaClass.name}#$deploymentID"
    }

    override val coroutineContext: CoroutineContext by lazy {
        context.dispatcher() +
                SupervisorJob() +
                VerticleName(name) +
                MDCContext(mapOf(VERTICLE_NAME_MDC to name)) +
                CoroutineExceptionHandler { _, ex ->
                    logger.lazy.error(throwable = ex) { "Unhandled exception" }
                }
    }

    private lateinit var configHolder: ConfigHolder<Config>

    /**
     * Config passed during deployment.
     */
    protected val typedConfig: Config
        get() = configHolder.config


    override fun init(vertx: Vertx, context: Context) {
        super.init(vertx, context)
        this.context = context
        vertigram = vertx.getVertigram(config.getString("vertigramName"))
        val wrapper = config.mapTo<ConfigWrapper<Config>>(
            vertigram.objectMapper.typeFactory.constructParametricType(ConfigWrapper::class.java, vertigram.objectMapper.constructType(configTypeReference.type)), vertigram.objectMapper
        )
        configHolder = ConfigHolder(wrapper.config)
    }

    /**
     * Non-reified version of [consumer]
     *
     * @see consumer
     * @see Vertigram.EventBus.consumer
     * @see io.vertx.core.eventbus.EventBus.consumer
     */
    inline fun <RequestPayload, Result> consumerNonReified(
        address: String,
        replyOptions: DeliveryOptions = DeliveryOptions(),
        requestJavaType: JavaType,
        crossinline function: suspend (RequestPayload) -> Result
    ) = vertigram.eventBus.consumerNonReified(
        coroScope = this,
        address = address,
        replyOptions = replyOptions,
        requestJavaType = requestJavaType,
        function = function
    )

    /**
     * Attach a consumer [function] to event bus on Vertigram [address] with the verticle as a coroutine scope.
     *
     * @see Vertigram.EventBus.consumer
     */
    inline fun <reified RequestPayload, Result> consumer(
        /**
         * Vertigram address
         */
        address: String,
        /**
         * Reply options
         */
        replyOptions: DeliveryOptions = DeliveryOptions(),
        /**
         * Request java type
         */
        requestJavaType: JavaType = vertigram.objectMapper.typeFactory.constructType(typeReference<RequestPayload>().type),
        /**
         * Consumer itself
         */
        crossinline function: suspend (RequestPayload) -> Result
    ) = vertigram.eventBus.consumer(
        coroScope = this,
        address = address,
        replyOptions = replyOptions,
        requestJavaType = requestJavaType,
        function = function
    )

    /**
     * Non-reified version of [localConsumer]
     *
     * @see consumer
     * @see Vertigram.EventBus.consumer
     * @see io.vertx.core.eventbus.EventBus.consumer
     */
    inline fun <RequestPayload, Result> localConsumerNonReified(
        address: String,
        replyOptions: DeliveryOptions = DeliveryOptions(),
        requestJavaType: JavaType,
        crossinline function: suspend (RequestPayload) -> Result
    ) = vertigram.eventBus.localConsumerNonReified(
        coroScope = this,
        address = address,
        replyOptions = replyOptions,
        requestJavaType = requestJavaType,
        function = function
    )

    /**
     * Attach a local consumer [function] to event bus on Vertigram [address] with the verticle as a coroutine scope.
     *
     * @see Vertigram.EventBus.localConsumer
     */
    inline fun <reified RequestPayload, Result> localConsumer(
        /**
         * Vertigram address
         */
        address: String,
        /**
         * Reply options
         */
        replyOptions: DeliveryOptions = DeliveryOptions(),
        /**
         * Request java type
         */
        requestJavaType: JavaType = vertigram.objectMapper.typeFactory.constructType(typeReference<RequestPayload>().type),
        /**
         * Consumer itself
         */
        crossinline function: suspend (RequestPayload) -> Result
    ) = vertigram.eventBus.localConsumer(
        coroScope = this,
        address = address,
        replyOptions = replyOptions,
        requestJavaType = requestJavaType,
        function = function
    )

    private data class BareBonesConfig(
        override val vertigramName: String
    ) : HasVertigramName

    /**
     * Wrapper for the config.
     *
     * See [Vertigram.deployVerticle] for more details about the protocol around bare Verticles
     */
    data class ConfigWrapper<Config>(
        /**
         * Vertigram name
         */
        override val vertigramName: String,
        /**
         * Config itself, as passed to [Vertigram.deployVerticle]
         */
        val config: Config
    ) : HasVertigramName

    private interface HasVertigramName {
        val vertigramName: String
    }


    private data class ConfigHolder<T>(val config: T)

    companion object {
        const val VERTICLE_NAME_MDC = "verticleName"
    }
}

private fun typeReferenceExample() {
    data class Config(val something: String)

    class ExampleVerticle : VertigramVerticle<Config>() {
        override val configTypeReference: TypeReference<Config> = typeReference()
    }
}