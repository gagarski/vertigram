package ski.gagar.vertigram.logback

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.uncheckedCast
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import java.util.concurrent.atomic.AtomicReference

class EventBusAppender : AppenderBase<ILoggingEvent>() {
    private var vertigram_: AtomicReference<Vertigram?> = AtomicReference(null)
    var address: String = "ski.gagar.vertigram.logback"
    val vertigram: Vertigram?
        get() = vertigram_.get()

    override fun append(eventObject: ILoggingEvent) {
        if (null != eventObject.mdcPropertyMap[BYPASS]) {
            return
        }
        vertigram?.eventBus?.publish(address, LogEvent(eventObject))
    }

    fun attachVertigram(vertigram: Vertigram) {
        vertigram_.set(vertigram)
    }

    companion object {
        const val BYPASS = "bypassEventBusAppender"
    }
}

fun Vertigram.attachEventBusAppender(appender: EventBusAppender) {
    appender.attachVertigram(this)
}

fun Vertigram.attachEventBusLogging() {
    val cxt = LoggerFactory.getILoggerFactory().uncheckedCast<LoggerContext>()
    var attached = false
    for (logger in cxt.loggerList) {
        for (appender in logger.iteratorForAppenders()) {
            if (appender is EventBusAppender) {
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
    withContext(MDCContext(contextMap = mapOf(EventBusAppender.BYPASS to EventBusAppender.BYPASS))) {
        block()
    }
}

inline fun bypassEventBusAppender(crossinline block: () -> Unit) {
    try {
        MDC.put(EventBusAppender.BYPASS, EventBusAppender.BYPASS)
        block()
    } finally {
        MDC.remove(EventBusAppender.BYPASS)
    }
}
