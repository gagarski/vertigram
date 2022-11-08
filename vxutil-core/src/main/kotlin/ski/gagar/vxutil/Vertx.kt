package ski.gagar.vxutil

import io.vertx.core.Context
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.slf4j.Logger
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

fun <T> Vertx.runBlocking(block: suspend Vertx.() -> T) {
    try {
        runBlocking(dispatcher()) {
            block()
        }
    } catch (t: Throwable) {
        logger.lazy.error(throwable = t) { "Error while runBlocking" }
        exitProcess(-1)
    }
}


data class CoroTimerHandle(val vertx: Vertx, val timerId: Long, var job: Job? = null) {
    fun cancel() {
        vertx.cancelTimer(timerId)
        job?.cancel()
    }
}
private fun Long.coroTimerHandle(vertx: Vertx) = CoroTimerHandle(vertx, this)
fun CoroutineVerticle.setTimerCoro(millis: Long, handler: suspend (timerId: Long) -> Unit): CoroTimerHandle {
    var handle: CoroTimerHandle? = null

    handle = this.vertx.setTimer(millis) { timerId ->
        handle!!.job = launch {
            if (!isActive) return@launch
            withContext(NonCancellable) {
                handler(timerId)
            }
        }
    }.coroTimerHandle(vertx)
    return handle
}


suspend fun <T> retrying(
    shouldStop: (Int) -> Boolean = { it >= 10 },
    coolDown: suspend (Int) -> Unit = { delay(5000) },
    block: suspend () -> T
): T {
    var i = 0
    while (!shouldStop(i)) {
        try {
            return block()
        } catch (e: Exception) {
            i++
            logger.lazy.error(throwable = e) { "Error while attempt #$i" }
            if (shouldStop(i))
                throw e
            else
                coolDown(i)
        }
    }
    throw AssertionError("Should never happen")
}

fun DeploymentOptions.setTypedConfig(obj: Any): DeploymentOptions =
    setConfig(JsonObject.mapFrom(obj))


abstract class ErrorLoggingCoroutineVerticle : CoroutineVerticle() {
    private lateinit var context: Context
    override val coroutineContext: CoroutineContext by lazy {
        context.dispatcher() + SupervisorJob() + CoroutineExceptionHandler { _, ex ->
            logger.lazy.error(throwable = ex) { "Unhandled exception" }
        }
    }

    override fun init(vertx: Vertx, context: Context) {
        super.init(vertx, context)
        this.context = context
    }

    fun suicide() {
        vertx.undeploy(deploymentID)
    }
}

fun Vertx.logUnhandledExceptions(logger: Logger = ski.gagar.vxutil.logger): Vertx = exceptionHandler {
    logger.lazy.error(throwable = it) { "Unhandled exception" }
}
