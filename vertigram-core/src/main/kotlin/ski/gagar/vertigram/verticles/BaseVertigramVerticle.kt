package ski.gagar.vertigram.verticles

import com.fasterxml.jackson.databind.DeserializationFeature
import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.slf4j.MDCContext
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.coroutines.VerticleName
import ski.gagar.vertigram.defaultVertigramMapper
import ski.gagar.vertigram.getVertigram
import ski.gagar.vertigram.jackson.mapTo
import ski.gagar.vertigram.lazy
import ski.gagar.vertigram.logger
import kotlin.coroutines.CoroutineContext

internal val BOOTSTRAP_JSON_MAPPER = defaultVertigramMapper()
    .copy()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

abstract class BaseVertigramVerticle : CoroutineVerticle(), Named {
    lateinit var vertigram: Vertigram
        private set

    private lateinit var context: Context

    override val name: String by lazy {
        "${this.javaClass.name}#$deploymentID"
    }

    override val coroutineContext: CoroutineContext by lazy {
        context.dispatcher() +
                SupervisorJob() +
                VerticleName(name) +
                MDCContext(mapOf(Named.VERTICLE_NAME_MDC to name)) +
                CoroutineExceptionHandler { _, ex ->
                    logger.lazy.error(throwable = ex) { "Unhandled exception" }
                }
    }

    override fun init(vertx: Vertx, context: Context) {
        super.init(vertx, context)
        this.context = context

        val bareBonesConfig = config.mapTo<BareBonesConfig>(BOOTSTRAP_JSON_MAPPER)
        vertigram = vertx.getVertigram(bareBonesConfig.vertigramName)
    }

    data class BareBonesConfig(
        val vertigramName: String
    )
}
