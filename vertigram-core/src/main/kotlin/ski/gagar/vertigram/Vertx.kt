package ski.gagar.vertigram

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
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


fun Vertx.logUnhandledExceptions(logger: Logger = ski.gagar.vertigram.util.logger): Vertx = exceptionHandler {
    logger.lazy.error(throwable = it) { "Unhandled exception" }
}
