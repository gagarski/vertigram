package ski.gagar.vxutil.vertigram.verticles.logging

import ski.gagar.vxutil.ErrorLoggingCoroutineVerticle
import ski.gagar.vxutil.jackson.mapTo
import ski.gagar.vxutil.jackson.suspendJsonConsumer
import ski.gagar.vxutil.logback.Level
import ski.gagar.vxutil.logback.LogEvent
import ski.gagar.vxutil.logback.asString
import ski.gagar.vxutil.logback.bypassEventBusAppenderSuspend
import ski.gagar.vxutil.setTimerCoro
import ski.gagar.vxutil.vertigram.client.DirectTelegram
import ski.gagar.vxutil.vertigram.client.THIN_POOLS
import ski.gagar.vxutil.vertigram.client.Telegram
import ski.gagar.vxutil.vertigram.sendMessage
import ski.gagar.vxutil.vertigram.types.Me
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.toChatId
import ski.gagar.vxutil.vertigram.util.md
import java.time.Duration

class TelegramLoggingVerticle : ErrorLoggingCoroutineVerticle() {
    private val typedConf: Config by lazy {
        config.mapTo()
    }

    private lateinit var tg: Telegram

    private var acc: MutableMap<Level, Int>? = null

    override suspend fun start() {
        tg = DirectTelegram(typedConf.token, vertx, typedConf.tgOptions)

        suspendJsonConsumer<LogEvent, Unit>(typedConf.listenAddress) {
            handleLogEvent(it)
        }
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
                chatId = typedConf.chatId.toChatId(),
                text = md {
                    +log.level.emoji
                    +" "
                    b {
                        +log.message.ellipsize(LINE_MAX_LENGTH)
                    }
                    br()

                    typedConf.me?.let {
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
                }.toString(),
                parseMode = ParseMode.MARKDOWN_V2
            )
            msgSent++

            val buf = StringBuilder()

            suspend fun flush(force: Boolean = false): Boolean {
                if (force || msgSent < MAX_SEQ_MESSAGES - 1) {
                    tg.sendMessage(
                        chatId = typedConf.chatId.toChatId(),
                        text = md {
                            pre(buf.trimEnd('\n').toString())
                        }.toString(),
                        parseMode = ParseMode.MARKDOWN_V2
                    )
                    buf.clear()
                    msgSent++
                    return false
                } else {
                    tg.sendMessage(
                        chatId = typedConf.chatId.toChatId(),
                        text = md {
                            pre(TRUNCATED)
                        }.toString(),
                        parseMode = ParseMode.MARKDOWN_V2
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
            chatId = typedConf.chatId.toChatId(),
            text = md {
                +"For the last ${typedConf.accumulationPeriod} the following log events occurred"
                typedConf.me?.let {
                    +" in ${it.username}"
                }
                +":"

                br()

                for ((level, count) in oldAcc) {
                    +"${level.emoji} — $count times"
                    br()
                }

                br() // extra after last
                +"Please consult logs for more details."
            }.toString(),
            parseMode = ParseMode.MARKDOWN_V2
        )
    }

    private suspend fun startAccumulating() {
        val period = typedConf.accumulationPeriod
        if (null == period || Duration.ZERO == period) {
            return
        }

        acc = sortedMapOf()

        setTimerCoro(period.toMillis()) {
            flushAcc()
        }
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

    data class Config(
        val token: String,
        val chatId: Long,
        val accumulationPeriod: Duration? = Duration.ofMinutes(5),
        val tgOptions: DirectTelegram.Options = DirectTelegram.Options(pools = THIN_POOLS),
        val me: Me? = null,
        val listenAddress: String = DEFAULT_LISTEN_ADDRESS
    )

    companion object {
        const val LINE_MAX_LENGTH = 200
        const val STACKTRACE_LIMIT_FIRST = 3000
        const val STACKTRACE_LIMIT = 3500
        const val MAX_SEQ_MESSAGES = 5
        const val TRUNCATED = "... truncated ..."
        const val DEFAULT_LISTEN_ADDRESS = "ski.gagar.vxutil.logback"
    }
}
