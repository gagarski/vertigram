package ski.gagar.vertigram.verticles.common

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
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

internal object InternalDeploymentMetadata {
    const val FIELD = "_vertigramInternal"
    const val PARENT_CHILD_DEATH_NOTICE_ADDRESS = "parentChildDeathNoticeAddress"
}

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

    private val configJavaType: JavaType by lazy {
        resolveConfigJavaType(vertigram.objectMapper.typeFactory)
    }

    /**
     * Resolve the deployment configuration type used for deserialization.
     *
     * Generic runtime verticle implementations may override this when their concrete [javaClass]
     * does not retain [Config].
     */
    protected open fun resolveConfigJavaType(typeFactory: TypeFactory): JavaType =
        typeFactory
            .constructType(javaClass)
            .findSuperType(VertigramVerticle::class.java)
            .containedTypeOrUnknown(0)

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
        val wrappedConfig = config.copy().apply {
            remove(InternalDeploymentMetadata.FIELD)
        }
        val wrapper = wrappedConfig.mapTo<ConfigWrapper<Config>>(
            vertigram.objectMapper.typeFactory.constructParametricType(ConfigWrapper::class.java, configJavaType), vertigram.objectMapper
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
     * @param address Vertigram address
     * @param replyOptions Reply options
     * @param requestJavaType Request java type
     * @param function Consumer itself
     *
     *  @see Vertigram.EventBus.consumer
     */
    inline fun <reified RequestPayload, Result> consumer(
        address: String,
        replyOptions: DeliveryOptions = DeliveryOptions(),
        requestJavaType: JavaType = vertigram.objectMapper.typeFactory.constructType(typeReference<RequestPayload>().type),
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
        address: String,
        replyOptions: DeliveryOptions = DeliveryOptions(),
        requestJavaType: JavaType = vertigram.objectMapper.typeFactory.constructType(typeReference<RequestPayload>().type),
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

