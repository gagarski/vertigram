package ski.gagar.vxutil

import io.vertx.core.Context
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.*
import org.slf4j.Logger
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

fun <T> Vertx.runBlocking(block: suspend Vertx.() -> T) {
    try {
        runBlocking(dispatcher()) {
            block()
        }
    } catch (t: Throwable) {
        globalLogger.error("", t)
        exitProcess(-1)
    }
}


suspend fun Vertx.sleep(millis: Long): Unit = awaitResult { h ->
    this.setTimer(millis) { h.handle(Future.succeededFuture()) }
}

fun CoroutineVerticle.setTimerCoro(millis: Long, handler: suspend (timerId: Long) -> Unit) =
    this.vertx.setTimer(millis) { timerId ->
        launch {
            handler(timerId)
        }
    }

suspend fun CoroutineVerticle.sleep(millis: Long) =
    vertx.sleep(millis)

suspend fun <T> Vertx.retrying(
    shouldStop: (Int) -> Boolean = { it >= 10 },
    coolDown: suspend (Int) -> Unit = { this.sleep(5000) },
    block: suspend () -> T
): T {
    var i = 0
    while (!shouldStop(i)) {
        try {
            return block()
        } catch (e: Exception) {
            i++
            logger.error(e.message, e)
            if (shouldStop(i))
                throw e
            else
                coolDown(i)
        }
    }
    throw AssertionError("Should never happen")
}

suspend fun <T> CoroutineVerticle.retrying(
    shouldStop: (Int) -> Boolean = { it >= 10 },
    coolDown: suspend (Int) -> Unit = { this.sleep(5000) },
    block: suspend () -> T
): T = vertx.retrying(shouldStop, coolDown, block)

fun DeploymentOptions.setTypedConfig(obj: Any): DeploymentOptions =
    setConfig(JsonObject.mapFrom(obj))


abstract class ErrorLoggingCoroutineVerticle : io.vertx.kotlin.coroutines.CoroutineVerticle() {
    private lateinit var context: Context
    override val coroutineContext: CoroutineContext by lazy {
        context.dispatcher() + SupervisorJob() + CoroutineExceptionHandler { _, ex ->
            logger.error("Unhandled exception", ex)
        }
    }

    override fun init(vertx: Vertx, context: Context) {
        super.init(vertx, context)
        this.context = context
    }
}

fun Vertx.logUnhandledExceptions(logger: Logger = globalLogger): Vertx = exceptionHandler {
    logger.error("Unhandled exception", it)
}
