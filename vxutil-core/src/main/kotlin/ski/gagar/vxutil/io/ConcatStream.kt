package ski.gagar.vxutil.io

import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.ReadStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ski.gagar.vxutil.uncheckedCast

private typealias RSW<T> = ReadStreamWrapper<T, ReadStream<T>>

class ConcatStream<T> internal constructor(private val scope: CoroutineScope,
                                           streams: Sequence<RSW<T>>) : ReadStream<T> {
    private enum class State {
        INITIAL,
        STARTED,
        WORKING,
        DONE
    }

    private var handler: Handler<T>? = null
    private var endHandler: Handler<Void?>? = null
    private var exceptionHandler: Handler<Throwable>? = null

    private val iter: Iterator<RSW<T>> = streams.iterator()

    private var paused = false
    private var state = State.INITIAL
    private var current: RSW<T>? = null
    private var demand: Long = Long.MAX_VALUE
    private var handled: Long = 0

    private inline fun doCatching(block: () -> Unit) {
        try {
            block()
        } catch (t: Throwable) {
            exceptionHandler?.handle(t)
        }
    }

    private fun nextOrNull() = iter.run {
        if (!hasNext()) null
        else next()
    }

    private fun fireEnd() {
        doCatching {
            endHandler?.handle(null)
        }
        demand = 0
        state = State.DONE
    }

    internal suspend fun prefetchFirst() {
        check(state == State.INITIAL)
        current = nextOrNull()
        current?.open()
        state = State.STARTED
    }

    private suspend fun switchStreams() {
        current?.close()
        val nextW = nextOrNull()
        nextW?.open()

        attachHandlers(nextW)
    }

    private fun attachHandlers(nextW: RSW<T>?) {
        val next = nextW?.get()

        if (next == null) {
            fireEnd()
            return
        }

        if (paused) {
            next.pause()
        }

        if (demand != Long.MAX_VALUE && demand != 0L) {
            next.fetch(demand)
        }

        next.exceptionHandler(exceptionHandler)

        next.endHandler {
            scope.launch {
                switchStreams()
            }
        }

        next.handler(demandTrackingHandler(handler))
    }

    override fun pause(): ReadStream<T> = apply {
        paused = true
        demand = 0
        current?.get()?.pause()
    }

    override fun resume(): ReadStream<T> = apply {
        paused = false
        demand = Long.MAX_VALUE
        current?.get()?.resume()
    }

    private fun incDemand(value: Long) {
        demand += value
        if (demand < 0) {
            demand = Long.MAX_VALUE
        }
    }

    override fun fetch(amount: Long): ReadStream<T> = apply {
        incDemand(amount)
        current?.get()?.fetch(demand)
    }

    override fun handler(handler: Handler<T>?): ReadStream<T> = apply {
        check(state != State.INITIAL) {
            "Stream should be started to attach handler"
        }
        this@ConcatStream.handler = handler
        when (state) {
            State.STARTED -> {
                state = State.WORKING
                attachHandlers(current)
            }
            State.WORKING -> {
                current?.get()?.handler(demandTrackingHandler(handler))
            }
            else -> {}
        }
    }

    private fun demandTrackingHandler(handler: Handler<T>?): Handler<T> = Handler {
        if (demand != 0L && demand != Long.MAX_VALUE) demand--
        handled += it.uncheckedCast<Buffer>().length()
        handler?.handle(it)
    }

    override fun exceptionHandler(exceptionHandler: Handler<Throwable>?): ReadStream<T> = apply {
        this.exceptionHandler = exceptionHandler
        if (state == State.WORKING) {
            current?.get()?.exceptionHandler(exceptionHandler)
        }
    }

    override fun endHandler(endHandler: Handler<Void?>?): ReadStream<T> = apply {
        this.endHandler = endHandler
    }
}


suspend fun <T> CoroutineScope.ConcatStream(streams: Sequence<RSW<T>>) =
    ConcatStream(this, streams).apply {
        prefetchFirst()
    }

suspend fun <T> ConcatStream(streams: Collection<RSW<T>>) = coroutineScope {
    ConcatStream(streams.asSequence())
}

suspend fun <T> ConcatStream(vararg streams: RSW<T>) = coroutineScope {
    ConcatStream(streams.asSequence())
}
