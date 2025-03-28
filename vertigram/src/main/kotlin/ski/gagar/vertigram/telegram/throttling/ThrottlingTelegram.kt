package ski.gagar.vertigram.telegram.throttling

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import kotlinx.coroutines.delay
import org.reflections.Reflections
import ski.gagar.vertigram.telegram.client.DirectTelegram
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.exceptions.TelegramCallException
import ski.gagar.vertigram.telegram.types.methods.TelegramCallable
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.util.toChatId
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import java.time.Duration
import java.time.Instant
import java.util.*

private val SECOND = Duration.ofSeconds(1)
private val MINUTE = Duration.ofMinutes(1)
private val CLEANUP_PERIOD = MINUTE.dividedBy(2)
private val CLEANUP_RETENTION = Duration.ofMinutes(1) + Duration.ofSeconds(15)

/**
 * Throttling implementation of [Telegram].
 *
 * On every method call it will do the following:
 *  - just throw [RateLimitExceededException] if you hit 429 from Telegram API
 *  - for non-[Throttled] method just call it
 *  - if the limits (global and per-chat) are not yet hit for [Throttled] method, just call it and register the call
 *  - if the limits are hit, then wait at most `maxWait` period or throw [RateLimitExceededException] if the wait will be too long
 */
class ThrottlingTelegram(
    private val vertx: Vertx,
    private val delegate: DirectTelegram,
    val throttling: ThrottlingOptions
) : Telegram by delegate, AutoCloseable {
    private val cleanUpTask = vertx.setPeriodic(CLEANUP_PERIOD.toMillis()) {
        cleanUp()
    }
    private val perChat: MutableMap<ChatId, NavigableSet<Instant>> = mutableMapOf()
    private val global: NavigableSet<Instant> = TreeSet()

    @Deprecated("Call Telegram.methodName() instead")
    override suspend fun <T> call(callable: TelegramCallable<T>): T {
        if (callable.javaClass !in TO_THROTTLE) {
            // Won't do any logic if it's a non-throttled-method
            translateRateLimit {
                @Suppress("DEPRECATION")
                return delegate.call(callable)
            }
        }
        var fromException: Duration? = null
        var waitingFor: Duration = Duration.ZERO

        while (true) {
            val preventiveSleep = getPreventiveDelay(callable, fromException)
            waitingFor += preventiveSleep

            if (waitingFor > throttling.maxWait) {
                logger.lazy.debug {
                    "We've wait for too long for $callable, giving up"
                }
                throw RateLimitExceededException("Max wait time will exceed in $waitingFor", retryIn = preventiveSleep)
            }

            if (!preventiveSleep.isZero) {
                logger.lazy.debug {
                    "Delaying $callable call for $preventiveSleep"
                }
                delay(preventiveSleep.toMillis())
            }

            try {
                val res = translateRateLimit {
                    @Suppress("DEPRECATION")
                    delegate.call(callable)
                }
                registerCall(callable)
                return res
            } catch (ex: RateLimitExceededException) {
                fromException = ex.retryIn
            }
        }
    }

    private fun registerCall(callable: TelegramCallable<*>) {
        val chatId = when (callable) {
            is HasChatId -> callable.chatId
            is HasChatIdLong -> callable.chatId?.toChatId()
            else -> null
        }

        val now = Instant.now()

        global.add(now)

        chatId?.let {
            perChat[chatId] = (perChat[chatId] ?: TreeSet()).apply {
                add(now)
            }
        }
    }

    private fun getThrottlingDuration(
        events: NavigableSet<Instant>, now: Instant, duration: Duration, n: Int, useLast: Boolean = false
    ): Duration? {
        require(n > 0)

        val throttlingEvents = events.tailSet(now - duration)

        if (throttlingEvents.size < n) {
            return null
        }

        val event = if (useLast) {
            throttlingEvents.last()!!
        } else {
            throttlingEvents.first()!!
        }

        return duration - (Duration.between(event, now))
    }

    private fun getPreventiveDelay(callable: TelegramCallable<*>, fromException: Duration?): Duration {
        val now = Instant.now()
        var toSleep = throttling.globalPerSecond?.let {
            getThrottlingDuration(global, now, SECOND, it)
        } ?: Duration.ZERO!!


        val chatId = when (callable) {
            is HasChatId -> callable.chatId
            is HasChatIdLong -> callable.chatId?.toChatId()
            else -> null
        }

        val chatEvents = chatId?.let { perChat[it] }

        if (!chatEvents.isNullOrEmpty()) {
            val perChatMinute =
                throttling.chatPerMinute?.let {
                    getThrottlingDuration(chatEvents, now, MINUTE, it)
                }

            if (null != perChatMinute && perChatMinute > toSleep) {
                toSleep = perChatMinute
            }

            val perChatSecond =
                throttling.chatBurstPerSecond?.let {
                    getThrottlingDuration(chatEvents, now, SECOND, it, true)
                }

            if (null != perChatSecond && perChatSecond > toSleep) {
                toSleep = perChatSecond
            }
        }

        if (fromException != null && fromException > toSleep) {
            toSleep = fromException
        }

        return toSleep
    }

    private inline fun <T> translateRateLimit(block: () -> T): T {
        try {
            return block()
        } catch (ex: TelegramCallException) {
            if (ex.status == HttpResponseStatus.TOO_MANY_REQUESTS.code()) {
                throw RateLimitExceededException("Rate limit exceeded, got 429", ex, getRetryIn(ex))
            } else {
                throw ex
            }
        }
    }

    private fun getRetryIn(ex: TelegramCallException): Duration? =
        ex.responseHeadersMultiMap[HttpHeaderNames.RETRY_AFTER]?.toLongOrNull()?.let { Duration.ofSeconds(it) }

    private fun cleanUp() {
        val now = Instant.now()
        global.headSet(now - CLEANUP_RETENTION).clear()

        val iter = perChat.iterator()

        while (iter.hasNext()) {
            val (_, c) = iter.next()
            c.headSet(now).clear()
            if (c.isEmpty()) {
                iter.remove()
            }
        }
    }

    override fun close() {
        vertx.cancelTimer(cleanUpTask)
    }

    companion object {
        private val TO_THROTTLE =
            Reflections("ski.gagar.vertigram.types.methods").getTypesAnnotatedWith(Throttled::class.java)
    }
}
