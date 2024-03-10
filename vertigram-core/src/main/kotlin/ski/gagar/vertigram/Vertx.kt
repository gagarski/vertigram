package ski.gagar.vertigram

import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import kotlin.system.exitProcess

/**
 * Wrapper for [kotlinx.coroutines.runBlocking] using [Vertx.dispatcher].
 */
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

/**
 * Call [block] until no exception is thrown or [shouldStop] returns true, all [coolDown] between attempts.
 *
 * @param shouldStop A predicate on number of the current attempt that determines if we should stop and throw even if exception was
 *     thrown last time. The exception in that case is being rethrown to the caller.
 * @param coolDown A suspend function being called between the attempts. The number of the attempt is passed to it.
 * @param block The function to call
 */
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

/**
 * Set [Vertx.exceptionHandler] to [logger]
 */
fun Vertx.logUnhandledExceptions(logger: Logger = ski.gagar.vertigram.util.logger): Vertx = exceptionHandler {
    logger.lazy.error(throwable = it) { "Unhandled exception" }
}
