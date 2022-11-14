package ski.gagar.vxutil

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.slf4j.MDCContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.lang.invoke.MethodHandles

val logger: Logger
    inline get() = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

val Logger.lazy
    get() = LazyLogger(this)

@JvmInline
value class LazyLogger(@PublishedApi internal val delegate: Logger) {
    inline fun trace(throwable: Throwable? = null, crossinline msgProvider: () -> String) {
        if (delegate.isTraceEnabled) {
            if (null == throwable)
                delegate.trace(msgProvider())
            else
                delegate.trace(msgProvider(), throwable)
        }
    }

    inline fun debug(throwable: Throwable? = null, crossinline msgProvider: () -> String) {
        if (delegate.isDebugEnabled) {
            if (null == throwable)
                delegate.debug(msgProvider())
            else
                delegate.debug(msgProvider(), throwable)
        }
    }

    inline fun info(throwable: Throwable? = null, crossinline msgProvider: () -> String) {
        if (delegate.isInfoEnabled) {
            if (null == throwable)
                delegate.info(msgProvider())
            else
                delegate.info(msgProvider(), throwable)
        }
    }

    inline fun warn(throwable: Throwable? = null, crossinline msgProvider: () -> String) {
        if (delegate.isWarnEnabled) {
            if (null == throwable)
                delegate.warn(msgProvider())
            else
                delegate.warn(msgProvider(), throwable)
        }
    }

    inline fun error(throwable: Throwable? = null, crossinline msgProvider: () -> String) {
        if (delegate.isErrorEnabled) {
            if (null == throwable)
                delegate.error(msgProvider())
            else
                delegate.error(msgProvider(), throwable)
        }
    }
}

fun CoroutineScope.coroMdcWith(vararg extra: Pair<String, String>) =
    (coroutineContext[MDCContext]?.contextMap ?: mapOf()).toMutableMap().apply {
        putAll(extra)
    }


inline fun withExtraMdc(extra: Map<String, String>, block: () -> Unit) {
    val old = extra.keys.asSequence().map { it to MDC.get(it) }.toMap()

    for ((k, v) in extra) {
        MDC.put(k, v)
    }
    try {
        block()
    } finally {
        for ((k, v) in old) {
            if (v != null) {
                MDC.put(k, v)
            } else {
                MDC.remove(k)
            }
        }
    }
}
