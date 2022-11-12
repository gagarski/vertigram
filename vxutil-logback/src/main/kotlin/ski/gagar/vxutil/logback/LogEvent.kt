package ski.gagar.vxutil.logback

import ch.qos.logback.classic.spi.ILoggingEvent

data class LogEvent(
    val level: Level,
    val threadName: String,
    val message: String,
    val loggerName: String,
    val mdcMap: Map<String, String>,
    val throwableProxy: ThrowableProxy? = null
)

fun LogEvent(orig: ILoggingEvent): LogEvent = LogEvent(
    level = orig.level.convert(),
    threadName = orig.threadName,
    message = orig.message,
    loggerName = orig.loggerName,
    mdcMap = orig.mdcPropertyMap.toMutableMap(),
    throwableProxy = ThrowableProxy(orig.throwableProxy)
)
