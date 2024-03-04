package ski.gagar.vertigram.util.io

import io.vertx.core.Handler
import io.vertx.core.streams.ReadStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private typealias RSW<T> = ReadStreamWrapper<T, ReadStream<T>>

/**
 * [ReadStream] which yields data from concatenated `streams`
 */
class ConcatStream<T> internal constructor(
    /**
     * Coroutine scope
     */
    private val scope: CoroutineScope,
    /**
     * Streams to concatenate
     */
    streams: Sequence<RSW<T>>
) : ReadStream<T> {
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
        current = nextW
        attachHandlers()
    }

    private fun attachHandlers() {
        val currentStream = current?.get()
        if (currentStream == null) {
            fireEnd()
            return
        }

        if (paused) {
            currentStream.pause()
        }

        if (demand != Long.MAX_VALUE && demand != 0L) {
            currentStream.fetch(demand)
        }

        currentStream.exceptionHandler(exceptionHandler)

        currentStream.endHandler {
            scope.launch {
                switchStreams()
            }
        }
        currentStream.handler(demandTrackingHandler(handler))
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
                attachHandlers()
            }
            State.WORKING -> {
                current?.get()?.handler(demandTrackingHandler(handler))
            }
            else -> {}
        }
    }

    private fun demandTrackingHandler(handler: Handler<T>?): Handler<T> = Handler {
        if (demand != 0L && demand != Long.MAX_VALUE) demand--
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

suspend fun <T> CoroutineScope.ConcatStream(streams: Collection<RSW<T>>) =
    ConcatStream(this, streams.asSequence()).apply {
        prefetchFirst()
    }
suspend fun <T> CoroutineScope.ConcatStream(vararg streams: RSW<T>) =
    ConcatStream(this, streams.asSequence()).apply {
        prefetchFirst()
    }

