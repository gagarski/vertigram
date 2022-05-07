package ski.gagar.vxutil.logback

import ch.qos.logback.classic.LoggerContext
import io.vertx.core.Vertx
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import ski.gagar.vxutil.lazy
import ski.gagar.vxutil.logger
import ski.gagar.vxutil.uncheckedCast
import java.util.concurrent.atomic.AtomicReference

class LocalEventBusAppender : AbstractEventBusAppender() {
    private var vertx_: AtomicReference<Vertx?> = AtomicReference(null)

    override val vertx: Vertx?
        get() = vertx_.get()

    fun attachVertx(vertx: Vertx) {
        vertx_.set(vertx)
    }
}

fun Vertx.attachEventBusAppender(appender: LocalEventBusAppender) {
    appender.attachVertx(this)
}

fun Vertx.attachEventBusLogging() {
    val cxt = LoggerFactory.getILoggerFactory().uncheckedCast<LoggerContext>()
    var attached = false
    for (logger in cxt.loggerList) {
        for (appender in logger.iteratorForAppenders()) {
            if (appender is LocalEventBusAppender) {
                appender.attachVertx(this)
                attached = true
            }
        }
    }

    if (!attached) {
        logger.lazy.error { "No loggers attached to vertx, check your logback config" }
    }
}

suspend inline fun bypassEventBusAppenderSuspend(crossinline block: suspend () -> Unit) {
    withContext(MDCContext(contextMap = mapOf(AbstractEventBusAppender.BYPASS to AbstractEventBusAppender.BYPASS))) {
        block()
    }
}

inline fun bypassEventBusAppender(crossinline block: () -> Unit) {
    try {
        MDC.put(AbstractEventBusAppender.BYPASS, AbstractEventBusAppender.BYPASS)
        block()
    } finally {
        MDC.remove(AbstractEventBusAppender.BYPASS)
    }
}
