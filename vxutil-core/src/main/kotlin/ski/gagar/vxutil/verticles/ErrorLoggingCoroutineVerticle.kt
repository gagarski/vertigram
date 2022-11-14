package ski.gagar.vxutil.verticles

import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.slf4j.MDCContext
import ski.gagar.vxutil.coroutines.VerticleName
import ski.gagar.vxutil.lazy
import ski.gagar.vxutil.logger
import kotlin.coroutines.CoroutineContext

abstract class ErrorLoggingCoroutineVerticle : CoroutineVerticle(), Named {
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
    }
}
