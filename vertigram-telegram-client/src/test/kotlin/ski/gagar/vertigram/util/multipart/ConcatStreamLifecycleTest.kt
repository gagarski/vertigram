package ski.gagar.vertigram.util.io

import io.vertx.core.Handler
import io.vertx.core.streams.ReadStream
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

object ConcatStreamLifecycleTest {
    @Test
    fun `source failure should close current stream and preserve the failure`() = runBlocking {
        val source = ManualReadStream<Int>()
        var closeCount = 0
        val wrapper = ReadStreamWrapper<Int, ReadStream<Int>>(
            provider = { source },
            closer = { closeCount++ }
        )
        val stream = this.ConcatStream(listOf(wrapper))
        val expected = RuntimeException("source failed")
        val observed = CompletableDeferred<Throwable>()

        stream.exceptionHandler { observed.complete(it) }
        stream.handler {}
        source.fail(expected)

        assertSame(expected, withTimeout(5_000) { observed.await() })
        assertEquals(1, closeCount)

        stream.close()
        assertEquals(1, closeCount)
    }

    @Test
    fun `failure opening next stream should close completed stream and be reported`() = runBlocking {
        val first = ManualReadStream<Int>()
        var firstCloseCount = 0
        val expected = RuntimeException("open failed")
        val observed = CompletableDeferred<Throwable>()
        val stream = this.ConcatStream(
            listOf(
                ReadStreamWrapper<Int, ReadStream<Int>>(
                    provider = { first },
                    closer = { firstCloseCount++ }
                ),
                ReadStreamWrapper(provider = { throw expected })
            )
        )

        stream.exceptionHandler { observed.complete(it) }
        stream.handler {}
        first.end()

        assertSame(expected, withTimeout(5_000) { observed.await() })
        assertEquals(1, firstCloseCount)
    }

    @Test
    fun `close failure should be suppressed by the source failure`() = runBlocking {
        val source = ManualReadStream<Int>()
        val expected = RuntimeException("source failed")
        val closeFailure = RuntimeException("close failed")
        val observed = CompletableDeferred<Throwable>()
        val stream = this.ConcatStream(
            listOf(
                ReadStreamWrapper<Int, ReadStream<Int>>(
                    provider = { source },
                    closer = { throw closeFailure }
                )
            )
        )

        stream.exceptionHandler { observed.complete(it) }
        stream.handler {}
        source.fail(expected)

        val actual = withTimeout(5_000) { observed.await() }
        assertSame(expected, actual)
        assertEquals(listOf(closeFailure), actual.suppressed.toList())
    }

    @Test
    fun `explicit close should be idempotent`() = runBlocking {
        val source = ManualReadStream<Int>()
        var closeCount = 0
        val stream = this.ConcatStream(
            listOf(
                ReadStreamWrapper<Int, ReadStream<Int>>(
                    provider = { source },
                    closer = { closeCount++ }
                )
            )
        )

        stream.close()
        stream.close()

        assertEquals(1, closeCount)
    }

    @Test
    fun `successive fetches should forward only incremental demand`() = runBlocking {
        val source = DemandReadStream((1..7).toList())
        val stream = this.ConcatStream(
            listOf(ReadStreamWrapper.ofNonCloseable(source))
        )
        val received = mutableListOf<Int>()
        val fiveReceived = CompletableDeferred<Unit>()

        stream.pause()
        stream.handler {
            received += it
            if (received.size == 5) fiveReceived.complete(Unit)
        }

        stream.fetch(2)
        stream.fetch(3)

        withTimeout(5_000) { fiveReceived.await() }
        assertEquals(listOf(2L, 3L), source.fetchAmounts)
        assertEquals(listOf(1, 2, 3, 4, 5), received)
    }

    @Test
    fun `remaining demand should carry over to the next source`() = runBlocking {
        val first = DemandReadStream(listOf(1))
        val second = DemandReadStream(listOf(2, 3, 4))
        val stream = this.ConcatStream(
            listOf(
                ReadStreamWrapper.ofNonCloseable(first),
                ReadStreamWrapper.ofNonCloseable(second)
            )
        )
        val received = mutableListOf<Int>()
        val threeReceived = CompletableDeferred<Unit>()

        stream.pause()
        stream.handler {
            received += it
            if (received.size == 3) threeReceived.complete(Unit)
        }
        stream.fetch(3)

        withTimeout(5_000) { threeReceived.await() }
        assertEquals(listOf(3L), first.fetchAmounts)
        assertEquals(listOf(2L), second.fetchAmounts)
        assertEquals(listOf(1, 2, 3), received)
    }

    @Test
    fun `flowing concat stream should resume a cold source`() = runBlocking {
        val source = DemandReadStream(listOf(1), initiallyPaused = true)
        val stream = this.ConcatStream(
            listOf(ReadStreamWrapper.ofNonCloseable(source))
        )
        val received = CompletableDeferred<Int>()

        stream.handler { received.complete(it) }

        assertEquals(1, withTimeout(5_000) { received.await() })
        assertEquals(1, source.resumeCount)
    }

    @Test
    fun `negative fetch should be rejected`() = runBlocking {
        val stream = this.ConcatStream(
            listOf(ReadStreamWrapper.ofNonCloseable(ManualReadStream<Int>()))
        )

        assertThrows(IllegalArgumentException::class.java) {
            stream.fetch(-1)
        }
    }

    private class ManualReadStream<T> : ReadStream<T> {
        private var exceptionHandler: Handler<Throwable>? = null
        private var endHandler: Handler<Void?>? = null

        override fun exceptionHandler(handler: Handler<Throwable>?): ReadStream<T> = apply {
            exceptionHandler = handler
        }

        override fun handler(handler: Handler<T>?): ReadStream<T> = this

        override fun pause(): ReadStream<T> = this

        override fun resume(): ReadStream<T> = this

        override fun fetch(amount: Long): ReadStream<T> = this

        override fun endHandler(handler: Handler<Void?>?): ReadStream<T> = apply {
            endHandler = handler
        }

        fun fail(throwable: Throwable) {
            requireNotNull(exceptionHandler).handle(throwable)
        }

        fun end() {
            requireNotNull(endHandler).handle(null)
        }
    }

    private class DemandReadStream<T>(
        values: List<T>,
        initiallyPaused: Boolean = false
    ) : ReadStream<T> {
        private val values = ArrayDeque(values)
        private var handler: Handler<T>? = null
        private var endHandler: Handler<Void?>? = null
        private var demand = if (initiallyPaused) 0L else Long.MAX_VALUE
        private var ended = false

        val fetchAmounts = mutableListOf<Long>()
        var resumeCount = 0
            private set

        override fun exceptionHandler(handler: Handler<Throwable>?): ReadStream<T> = this

        override fun handler(handler: Handler<T>?): ReadStream<T> = apply {
            this.handler = handler
            drain()
        }

        override fun pause(): ReadStream<T> = apply {
            demand = 0
        }

        override fun resume(): ReadStream<T> = apply {
            resumeCount++
            demand = Long.MAX_VALUE
            drain()
        }

        override fun fetch(amount: Long): ReadStream<T> = apply {
            require(amount >= 0)
            fetchAmounts += amount
            if (demand != Long.MAX_VALUE) {
                demand = if (amount > Long.MAX_VALUE - demand) {
                    Long.MAX_VALUE
                } else {
                    demand + amount
                }
            }
            drain()
        }

        override fun endHandler(handler: Handler<Void?>?): ReadStream<T> = apply {
            endHandler = handler
            drain()
        }

        private fun drain() {
            val handler = handler ?: return
            while (values.isNotEmpty() && demand != 0L) {
                if (demand != Long.MAX_VALUE) demand--
                handler.handle(values.removeFirst())
            }
            if (!ended && values.isEmpty()) {
                ended = true
                endHandler?.handle(null)
            }
        }
    }
}
