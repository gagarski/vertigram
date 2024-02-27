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
import ski.gagar.vertigram.jackson.mapTo
import ski.gagar.vertigram.jackson.typeReference
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import kotlin.coroutines.CoroutineContext


abstract class VertigramVerticle<Config> : CoroutineVerticle() {
    lateinit var vertigram: Vertigram
        private set

    protected abstract val configTypeReference: TypeReference<Config>

    private lateinit var context: Context

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

    data class BareBonesConfig(
        override val vertigramName: String
    ) : HasVertigramName

    data class ConfigWrapper<Config>(
        override val vertigramName: String,
        val config: Config
    ) : HasVertigramName

    interface HasVertigramName {
        val vertigramName: String
    }

    private data class ConfigHolder<T>(val config: T)

    companion object {
        const val VERTICLE_NAME_MDC = "verticleName"
    }

}