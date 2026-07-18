package ski.gagar.vertigram.logback

/**
 * A duplicate of [LogbackLevel] used in [LogEvent]
 */
enum class Level {
    OFF,
    ERROR,
    WARN,
    INFO,
    DEBUG,
    TRACE,
    ALL
}

private typealias LogbackLevel = ch.qos.logback.classic.Level

/**
 * Convert [LogbackLevel] to [Level]
 */
fun LogbackLevel.convert(): Level = when (LogbackLevel.toLevel(levelInt).levelInt) {
    LogbackLevel.OFF_INT -> Level.OFF
    LogbackLevel.ERROR_INT -> Level.ERROR
    LogbackLevel.WARN_INT -> Level.WARN
    LogbackLevel.INFO_INT -> Level.INFO
    LogbackLevel.DEBUG_INT -> Level.DEBUG
    LogbackLevel.TRACE_INT -> Level.TRACE
    LogbackLevel.ALL_INT -> Level.ALL
    else -> throw AssertionError("Should not happen")
}
