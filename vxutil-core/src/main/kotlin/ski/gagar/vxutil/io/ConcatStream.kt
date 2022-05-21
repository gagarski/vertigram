package ski.gagar.vxutil.io

import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.ReadStream
import ski.gagar.vxutil.uncheckedCast

class ConcatStream<T>(streams: Sequence<ReadStream<T>>) : ReadStream<T> {
    enum class State {
        INITIAL,
        WORKING,
        DONE
    }

    private var handler: Handler<T>? = null
    private var endHandler: Handler<Void?>? = null
    private var exceptionHandler: Handler<Throwable>? = null

    private val iter: Iterator<ReadStream<T>> = streams.iterator()

    private var paused = false
    private var state = State.INITIAL
    private var current: ReadStream<T>? = null
    private var demand: Long = 0
    private var handled: Long = 0

    constructor(streams: Collection<ReadStream<T>>) : this(streams.asSequence())
    constructor(vararg streams: ReadStream<T>) : this(streams.asSequence())

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

    private fun switchStreams() {
        val next = nextOrNull()
        current = next

        if (next == null) {
            fireEnd()
            return
        }

        next.exceptionHandler(exceptionHandler)

        next.endHandler {
            switchStreams()
        }

        next.handler(demandTrackingHandler(handler))

        if (demand != 0L && demand != Long.MAX_VALUE) {
            next.fetch(demand)
        }
    }

    override fun pause(): ReadStream<T> = apply {
        paused = true
        current?.pause()
    }

    override fun resume(): ReadStream<T> = apply {
        paused = false
        demand = Long.MAX_VALUE
        when (state) {
            State.DONE -> return this
            State.INITIAL -> switchStreams()
            else -> {}
        }
        current?.resume()
    }

    override fun fetch(amount: Long): ReadStream<T> = apply {
        paused = false
        demand += amount
        if (state == State.INITIAL) {
            switchStreams()
        }
        current?.fetch(demand)
    }

    override fun handler(handler: Handler<T>?): ReadStream<T> = apply {
        this@ConcatStream.handler = handler
        when (state) {
            State.INITIAL -> {
                state = State.WORKING
                switchStreams()
            }
            State.WORKING -> {
                current?.handler(demandTrackingHandler(handler))
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
            current?.exceptionHandler(exceptionHandler)
        }
    }

    override fun endHandler(endHandler: Handler<Void?>?): ReadStream<T> = apply {
        this.endHandler = endHandler
    }
}
