package ski.gagar.vertigram.logback

import ch.qos.logback.classic.spi.ILoggingEvent

/**
 * A json-serializable version of log event
 */
data class LogEvent(
    val level: Level,
    val threadName: String,
    val message: String,
    val loggerName: String,
    val mdcMap: Map<String, String>,
    val throwableProxy: ThrowableProxy? = null
)

/**
 * Converter from [ILoggingEvent] to [LogEvent]
 */
fun LogEvent(orig: ILoggingEvent): LogEvent = LogEvent(
    level = orig.level.convert(),
    threadName = orig.threadName,
    message = orig.message,
    loggerName = orig.loggerName,
    mdcMap = orig.mdcPropertyMap.toMutableMap(),
    throwableProxy = ThrowableProxy(orig.throwableProxy)
)
