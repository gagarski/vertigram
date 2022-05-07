package ski.gagar.vxutil

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

val logger: Logger
    inline get() = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

val Logger.lazy
    get() = LazyLogger(this)

// TODO implement Logger by delegation when it's supported https://youtrack.jetbrains.com/issue/KT-27435
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
