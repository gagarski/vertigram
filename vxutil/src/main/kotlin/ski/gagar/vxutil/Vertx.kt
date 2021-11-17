package ski.gagar.vxutil

import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.WorkerExecutor
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.*
import org.slf4j.Logger
import java.util.concurrent.CompletableFuture

fun <T> Vertx.runBlocking(block: suspend Vertx.() -> T): Unit =
    kotlinx.coroutines.runBlocking {
        launch(dispatcherWithEx) {
            block()
        }
    }

suspend fun Vertx.sleep(millis: Long): Unit = awaitResult { h ->
    this.setTimer(millis) { h.handle(Future.succeededFuture()) }
}

fun CoroutineVerticle.setTimerCoro(millis: Long, handler: suspend (timerId: Long) -> Unit) =
    this.vertx.setTimer(millis) { timerId ->
        launch(this.vertx.dispatcherWithEx(this.logger)) {
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


suspend fun <T> WorkerExecutor.executeBlockingSuspend(blocking: suspend () -> T): Future<T> =
    executeBlocking { promise ->
        // We do not use vertx.dispatcher here. Instead we go with thread-confined dispatcher fopr runBlocking
        runBlocking {
            try {
                promise.complete(blocking())
            } catch (e: Throwable) {
                promise.fail(e)
            }
        }
    }


fun DeploymentOptions.setTypedConfig(obj: Any): DeploymentOptions =
    setConfig(JsonObject.mapFrom(obj))

fun Vertx.dispatcherWithEx(logger: Logger) =
    dispatcher() + CoroutineExceptionHandler { _, ex ->
        logger.error("Unhandled exception", ex)
    }

val Vertx.dispatcherWithEx
    get() = dispatcherWithEx(globalLogger)


fun Vertx.logExceptions(logger: Logger = globalLogger): Vertx = exceptionHandler {
    logger.error("Unhandled exception", it)
}
