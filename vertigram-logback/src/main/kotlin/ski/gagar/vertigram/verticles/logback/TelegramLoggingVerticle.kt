package ski.gagar.vertigram.verticles.logback

import com.fasterxml.jackson.core.type.TypeReference
import kotlinx.coroutines.Job
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.coroutines.setTimerNonCancellable
import ski.gagar.vertigram.jackson.typeReference
import ski.gagar.vertigram.logback.Level
import ski.gagar.vertigram.logback.LogEvent
import ski.gagar.vertigram.logback.asString
import ski.gagar.vertigram.logback.bypassEventBusAppenderSuspend
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.client.ThinTelegram
import ski.gagar.vertigram.telegram.markup.textMarkdown
import ski.gagar.vertigram.telegram.methods.sendMessage
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.telegram.types.util.toChatId
import ski.gagar.vertigram.verticles.common.VertigramVerticle
import ski.gagar.vertigram.verticles.logback.TelegramLoggingVerticle.Config
import java.time.Duration

/**
 * Verticle that sends log events produced by [ski.gagar.vertigram.logback.EventBusAppender] to selected Telegram chat.
 *
 * The _first_ logging event appears in the chat immediately.
 * Then (if [Config.accumulationPeriod] is set), the log events are accumulated during this period and the stats
 * are sent in the end of the period.
 * After the end of accumulation period, the next log event considered _first_ again.
 */
class TelegramLoggingVerticle : VertigramVerticle<TelegramLoggingVerticle.Config>() {
    override val configTypeReference: TypeReference<Config> = typeReference()
    private lateinit var tg: Telegram

    private var acc: MutableMap<Level, Int>? = null
    private var timer: Job? = null
    override suspend fun start() {
        tg = ThinTelegram(vertigram)

        consumer<LogEvent, Unit>(typedConfig.listenAddress) {
            handleLogEvent(it)
        }
    }

    override suspend fun stop() {
        cancelTimer()
    }

    private fun String.ellipsize(max: Int) = when {
        max <= 3 -> this
        length <= max -> this
        else -> "${substring(0, max - 3)}..."
    }

    private suspend fun sendExplicit(log: LogEvent) {
        bypassEventBusAppenderSuspend {
            val stackTraceLines = log.throwableProxy?.asString()?.lines()
            var stackTraceLinesProcessed = 0
            var msgSent = 0

            tg.sendMessage(
                chatId = typedConfig.chatId.toChatId(),
                richText = textMarkdown {
                    +log.level.emoji
                    +" "
                    b {
                        +log.message.ellipsize(LINE_MAX_LENGTH)
                    }
                    br()

                    typedConfig.me?.let {
                        b {
                            +"Bot: "
                        }

                        +"@${it.username}" // null should not happen for bots
                    }

                    br()

                    b {
                        +"Logger: "
                    }
                    +log.loggerName.ellipsize(LINE_MAX_LENGTH)
                    br()
                    b {
                        +"Thread: "
                    }
                    +log.threadName.ellipsize(LINE_MAX_LENGTH)
                    br()

                    val verticleName = log.mdcMap[VERTICLE_NAME_MDC]

                    if (null != verticleName) {
                        b {
                            +"Verticle: "
                        }
                        +verticleName.ellipsize(LINE_MAX_LENGTH)
                        br()
                    }

                    val address = log.mdcMap[Vertigram.CONSUMER_ADDRESS_MDC]

                    if (null != address) {
                        b {
                            +"Address: "
                        }
                        +address.ellipsize(LINE_MAX_LENGTH)
                        br()
                    }


                    br()

                    stackTraceLines?.let {
                        val firstPart = buildString {
                            for (line in stackTraceLines) {
                                val toAppend = "${line.ellipsize(LINE_MAX_LENGTH)}\n"
                                if (length + toAppend.length > STACKTRACE_LIMIT_FIRST) {
                                    break
                                }
                                append(toAppend)
                                stackTraceLinesProcessed++
                            }
                            trimEnd('\n')
                        }

                        pre(firstPart)
                    }
                }
            )
            msgSent++

            val buf = StringBuilder()

            suspend fun flush(force: Boolean = false): Boolean {
                if (force || msgSent < MAX_SEQ_MESSAGES - 1) {
                    tg.sendMessage(
                        chatId = typedConfig.chatId.toChatId(),
                        richText = textMarkdown {
                            pre(buf.trimEnd('\n').toString())
                        }
                    )
                    buf.clear()
                    msgSent++
                    return false
                } else {
                    tg.sendMessage(
                        chatId = typedConfig.chatId.toChatId(),
                        richText = textMarkdown {
                            pre(TRUNCATED)
                        }
                    )
                    buf.clear()
                    // msgSent++
                    return true
                }
            }

            for (line in stackTraceLines?.asSequence()?.drop(stackTraceLinesProcessed) ?: sequenceOf()) {
                val withNl = "${line.ellipsize(LINE_MAX_LENGTH)}\n"
                val msgFull = buf.length + withNl.length > STACKTRACE_LIMIT

                if (msgFull && flush()) {
                    break
                }

                buf.append(withNl)
                stackTraceLinesProcessed++
            }

            if (buf.isNotEmpty()) {
                flush(true)
            }
        }
    }

    private suspend fun flushAcc() {
        val oldAcc = acc ?: return
        acc = null

        if (oldAcc.isEmpty()) {
            return
        }

        tg.sendMessage(
            chatId = typedConfig.chatId.toChatId(),
            richText = textMarkdown {
                +"For the last ${typedConfig.accumulationPeriod} the following log events occurred"
                typedConfig.me?.let {
                    +" in @${it.username}"
                }
                +":"

                br()

                for ((level, count) in oldAcc) {
                    +"${level.emoji} — $count times"
                    br()
                }

                br() // extra after last
                +"Please consult logs for more details."
            }
        )
    }

    private suspend fun startAccumulating() {
        val period = typedConfig.accumulationPeriod
        if (null == period || Duration.ZERO == period) {
            return
        }

        acc = sortedMapOf()

        timer = setTimerNonCancellable(period) {
            flushAcc()
        }
    }

    private fun cancelTimer() {
        timer?.cancel()
    }

    private fun accumulate(log: LogEvent) {
        val acc = this.acc ?: return
        acc[log.level] = (acc[log.level] ?: 0) + 1
    }

    private suspend fun handleLogEvent(log: LogEvent) {
        bypassEventBusAppenderSuspend {
            if (null == acc) {
                startAccumulating()
                sendExplicit(log)
            } else {
                accumulate(log)
            }
        }
    }

    private val Level.emoji
        get() = when (this) {
            Level.OFF -> "⚫"
            Level.ERROR -> "\uD83D\uDD34"
            Level.WARN -> "\uD83D\uDFE1"
            Level.INFO -> "\uD83D\uDFE2"
            Level.DEBUG -> "\uD83D\uDD35"
            Level.TRACE -> "⚪"
            Level.ALL -> "⚪"
        }

    /**
     * Config for [TelegramLoggingVerticle]
     */
    data class Config(
        /**
         * Id of the chat to log
         */
        val chatId: Long,
        /**
         * Period of which the events (after first one) are accumulated.
         */
        val accumulationPeriod: Duration? = Duration.ofMinutes(5),
        /**
         * Bot user info (returned from `getMe`)
         */
        val me: User.Me? = null,
        /**
         * An address to listen to.
         * @see ski.gagar.vertigram.logback.EventBusAppender.address
         */
        val listenAddress: String = DEFAULT_LISTEN_ADDRESS
    )

    companion object {
        const val LINE_MAX_LENGTH = 200
        const val STACKTRACE_LIMIT_FIRST = 3000
        const val STACKTRACE_LIMIT = 3500
        const val MAX_SEQ_MESSAGES = 5
        const val TRUNCATED = "... truncated ..."
        const val DEFAULT_LISTEN_ADDRESS = "ski.gagar.vertigram.logback"
    }
}

suspend fun Vertigram.enableTelegramLogging(config: TelegramLoggingVerticle.Config) =
    deployVerticle(TelegramLoggingVerticle(), config)