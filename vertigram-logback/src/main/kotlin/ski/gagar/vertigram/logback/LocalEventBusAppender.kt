package ski.gagar.vertigram.logback

import ch.qos.logback.classic.LoggerContext
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.lazy
import ski.gagar.vertigram.logger
import ski.gagar.vertigram.uncheckedCast
import java.util.concurrent.atomic.AtomicReference

class LocalEventBusAppender : AbstractEventBusAppender() {
    private var vertigram_: AtomicReference<Vertigram?> = AtomicReference(null)

    override val vertigram: Vertigram?
        get() = vertigram_.get()

    fun attachVertigram(vertigram: Vertigram) {
        vertigram_.set(vertigram)
    }
}

fun Vertigram.attachEventBusAppender(appender: LocalEventBusAppender) {
    appender.attachVertigram(this)
}

fun Vertigram.attachEventBusLogging() {
    val cxt = LoggerFactory.getILoggerFactory().uncheckedCast<LoggerContext>()
    var attached = false
    for (logger in cxt.loggerList) {
        for (appender in logger.iteratorForAppenders()) {
            if (appender is LocalEventBusAppender) {
                appender.attachVertigram(this)
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
