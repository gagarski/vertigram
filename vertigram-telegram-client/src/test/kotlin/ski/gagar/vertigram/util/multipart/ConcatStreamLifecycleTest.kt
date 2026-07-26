package ski.gagar.vertigram.util.io

import io.vertx.core.Handler
import io.vertx.core.streams.ReadStream
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
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
}
